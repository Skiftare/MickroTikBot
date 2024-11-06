package edu.Data;

import edu.Configuration.DataConnectConfigurator;
import edu.Data.dto.ClientTransfer;
import edu.models.UserProfileStatus;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.responses.TransactionResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JdbcDataManagerTest extends IntegrationTest {
    private static final String url = POSTGRES.getJdbcUrl();
    private static final String user = POSTGRES.getUsername();
    private static final String password = POSTGRES.getPassword();

    private static DataConnectConfigurator dataConnection = new DataConnectConfigurator(url, user, password);
    private static JdbcDataManager jdbcDataManager = new JdbcDataManager(dataConnection);
    private static ClientTransfer clientTransferUserFirst = new ClientTransfer(
            null, 12345L, "1234567890", "Test User", new Date(System.currentTimeMillis()), "vpn_profile_1", true, new Date(System.currentTimeMillis() + 123444), false, "test memo mock", BigDecimal.ZERO
    );

    @AfterEach
    public void tearDown() {
        jdbcDataManager.getAllUsers().forEach(user -> jdbcDataManager.deleteById(user.tgUserId()));
    }


    @Test
    public void testSaveAndFindById() {
        jdbcDataManager.save(clientTransferUserFirst);
        ClientTransfer foundClient = jdbcDataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(foundClient);
        assertEquals(clientTransferUserFirst.tgUserId(), foundClient.tgUserId());
    }

    @Test
    public void testIsUserExists() {
        jdbcDataManager.save(clientTransferUserFirst);
        assertTrue(jdbcDataManager.isUserExists(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testAddUser() {
        jdbcDataManager.addUser(clientTransferUserFirst);
        ClientTransfer foundClient = jdbcDataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(foundClient);
    }

    @Test
    public void testUpdateUserPhoneByTelegramId() {
        jdbcDataManager.save(clientTransferUserFirst);
        String newPhone = "0987654321";
        jdbcDataManager.updateUserPhoneByTelegramId(clientTransferUserFirst.tgUserId(), newPhone);
        ClientTransfer updatedClient = jdbcDataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(updatedClient);
        assertEquals(newPhone, updatedClient.phone());
    }

    @Test
    public void testDeleteById() {
        jdbcDataManager.save(clientTransferUserFirst);
        jdbcDataManager.deleteById(clientTransferUserFirst.tgUserId());
        assertFalse(jdbcDataManager.isUserExists(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testGetAllUsers() {
        jdbcDataManager.save(clientTransferUserFirst);
        List<ClientTransfer> allUsers = jdbcDataManager.getAllUsers();
        assertTrue(allUsers.size() > 0);
        assertTrue(allUsers.stream().anyMatch(user -> user.tgUserId().equals(clientTransferUserFirst.tgUserId())));
    }

    @Test
    public void testGetUserProfileStatus() {
        jdbcDataManager.save(clientTransferUserFirst);
        jdbcDataManager.update(clientTransferUserFirst);
        assertEquals(UserProfileStatus.ACTIVE_VPN, jdbcDataManager.getUserProfileStatus(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testGetUserProfileStatusWithNoPhone() {
        jdbcDataManager.save(clientTransferUserFirst);
        jdbcDataManager.updateUserPhoneByTelegramId(clientTransferUserFirst.tgUserId(), null);
        assertEquals(UserProfileStatus.UNCONFIRMED, jdbcDataManager.getUserProfileStatus(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testGetUserProfileStatusWithNonExistentUser() {
        assertEquals(UserProfileStatus.GUEST, jdbcDataManager.getUserProfileStatus(99999L));
    }

    @Test
    public void testUpdate() {
        jdbcDataManager.save(clientTransferUserFirst);
        ClientTransfer updatedClientTransfer = new ClientTransfer(
                clientTransferUserFirst.id(),
                clientTransferUserFirst.tgUserId(),
                "1234567890",
                "Updated User",
                new Date(System.currentTimeMillis()),
                "vpn_profile_updated",
                false,
                new Date(System.currentTimeMillis()),
                false,
                "0",
                new BigDecimal(0)
        );
        jdbcDataManager.update(updatedClientTransfer);
        ClientTransfer foundClient = jdbcDataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(foundClient);
        assertEquals("Updated User", foundClient.name());
    }


    @Test
    public void testExtendVpnProfile() {
        // Создаем клиента с истекшим VPN
        Date expiredDate = new Date(System.currentTimeMillis() - 86400000); // вчера
        ClientTransfer expiredClient = new ClientTransfer(
            null, 12346L, "1234567890", "Test User", 
            new Date(System.currentTimeMillis()), 
            "vpn_profile_1", false, 
            expiredDate, false, "test_key", BigDecimal.ZERO
        );
        jdbcDataManager.save(expiredClient);

        // Продлеваем VPN на 30 дней
        boolean extended = jdbcDataManager.extendVpnProfile(
            expiredClient.tgUserId(), 
            Duration.ofDays(30)
        );
        
        assertTrue(extended);
        ClientTransfer updatedClient = jdbcDataManager.findById(expiredClient.tgUserId());
        assertTrue(updatedClient.isVpnProfileAlive());
    }

    @Test
    public void testUpdateUserPhoneAndHash() {
        jdbcDataManager.save(clientTransferUserFirst);
        String newPhone = "9876543210";
        String newHash = "new_hash_value";
        
        jdbcDataManager.updateUserPhoneAndHash(
            clientTransferUserFirst.tgUserId(), 
            newPhone, 
            newHash
        );
        
        ClientTransfer updatedClient = jdbcDataManager.findById(clientTransferUserFirst.tgUserId());
        assertEquals(newPhone, updatedClient.phone());
        assertEquals(newHash, updatedClient.paymentKey());
    }

    @Test
    public void testAddIncomingTransaction() {
        Memo mockMemo = mock(Memo.class);
        jdbcDataManager.getAllUsers().forEach(user -> jdbcDataManager.deleteById(user.tgUserId()));
        when(mockMemo.toString()).thenReturn("test memo mock");
        jdbcDataManager.save(clientTransferUserFirst);

        // Создаем мок PaymentOperationResponse
        PaymentOperationResponse mockPayment = mock(PaymentOperationResponse.class);
        TransactionResponse mockTransactionResponse = mock(TransactionResponse.class);


        Logger.getAnonymousLogger().info("Mock memo: " + mockMemo.toString());

        when(mockPayment.getTo()).thenReturn("test_destination");
        when(mockPayment.getTransactionHash()).thenReturn("test_hash");
        when(mockPayment.getSourceAccount()).thenReturn("test_source");
        when(mockPayment.getAmount()).thenReturn("100.0000000");
        when(mockPayment.getTransaction()).thenReturn(Optional.of(mockTransactionResponse));
        when(mockTransactionResponse.isSuccessful()).thenReturn(true);
        when(mockPayment.getTransaction().get().getMemo()).thenReturn(mockMemo);

        jdbcDataManager.addIncomingTransaction(mockPayment);
        
        // Проверяем, что транзакция добавлена
        try (Connection conn = dataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM stellar_transactions WHERE transaction_hash = ?"
             )) {
            stmt.setString(1, "test_hash");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("test memo mock", rs.getString("memo"));
        } catch (SQLException e) {
            fail("SQL Exception: " + e.getMessage());
        }
    }

    // Вспомогательный метод для создания мока PaymentOperationResponse

}