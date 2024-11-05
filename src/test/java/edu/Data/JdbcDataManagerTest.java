package edu.Data;

import edu.Configuration.DataConnectConfigurator;
import edu.Data.dto.ClientTransfer;
import edu.models.UserProfileStatus;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;
/*
public class JdbcDataManagerTest extends IntegrationTest {
    private static final String url = POSTGRES.getJdbcUrl();
    private static final String user = POSTGRES.getUsername();
    private static final String password = POSTGRES.getPassword();

    private static DataConnectConfigurator dataConnection = new DataConnectConfigurator(url, user, password);
    private static JdbcDataManager jdbcDataManager = new JdbcDataManager(dataConnection);
    private static ClientTransfer clientTransferUserFirst = new ClientTransfer(
            null, 12345L, "1234567890", "Test User", new Date(System.currentTimeMillis()), "vpn_profile_1", true, new Date(System.currentTimeMillis() + 123444)
    );

    @AfterEach
    public void tearDown() {
        // Test code for tearDown method
        jdbcDataManager.deleteById(clientTransferUserFirst.tgUserId());
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
        assertEquals(UserProfileStatus.UNCONFIRMED, jdbcDataManager.getUserProfileStatus(clientTransferUserFirst.tgUserId()));
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
        assertEquals("Test User", foundClient.name());
    }
}
*/