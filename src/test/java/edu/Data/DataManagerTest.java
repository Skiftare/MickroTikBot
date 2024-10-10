package edu.Data;

import edu.Configuration.DataConnectConfigurator;
import edu.Data.dto.ClientTransfer;
import edu.models.UserProfileStatus;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DataManagerTest extends IntegrationTest {
    private static final String url = POSTGRES.getJdbcUrl();
    private static final String user = POSTGRES.getUsername();
    private static final String password = POSTGRES.getPassword();

    private static DataConnectConfigurator dataConnection = new DataConnectConfigurator(url, user, password);
    private static DataManager dataManager = new DataManager(dataConnection);
    private static ClientTransfer clientTransferUserFirst = new ClientTransfer(
            null, 12345L, "1234567890", "Test User", new Date(System.currentTimeMillis()), "vpn_profile_1", true, new Date(System.currentTimeMillis() + 123444)
    );

    @AfterEach
    public void tearDown() {
        // Test code for tearDown method
        dataManager.deleteById(clientTransferUserFirst.tgUserId());
    }


    @Test
    public void testSaveAndFindById() {
        dataManager.save(clientTransferUserFirst);
        ClientTransfer foundClient = dataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(foundClient);
        assertEquals(clientTransferUserFirst.tgUserId(), foundClient.tgUserId());
    }

    @Test
    public void testIsUserExists() {
        dataManager.save(clientTransferUserFirst);
        assertTrue(dataManager.isUserExists(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testAddUser() {
        dataManager.addUser(clientTransferUserFirst);
        ClientTransfer foundClient = dataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(foundClient);
    }

    @Test
    public void testUpdateUserPhoneByTelegramId() {
        dataManager.save(clientTransferUserFirst);
        String newPhone = "0987654321";
        dataManager.updateUserPhoneByTelegramId(clientTransferUserFirst.tgUserId(), newPhone);
        ClientTransfer updatedClient = dataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(updatedClient);
        assertEquals(newPhone, updatedClient.phone());
    }

    @Test
    public void testDeleteById() {
        dataManager.save(clientTransferUserFirst);
        dataManager.deleteById(clientTransferUserFirst.tgUserId());
        assertFalse(dataManager.isUserExists(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testGetAllUsers() {
        dataManager.save(clientTransferUserFirst);
        List<ClientTransfer> allUsers = dataManager.getAllUsers();
        assertTrue(allUsers.size() > 0);
        assertTrue(allUsers.stream().anyMatch(user -> user.tgUserId().equals(clientTransferUserFirst.tgUserId())));
    }

    @Test
    public void testGetUserProfileStatus() {
        dataManager.save(clientTransferUserFirst);
        dataManager.update(clientTransferUserFirst);
        assertEquals(UserProfileStatus.ACTIVE_VPN, dataManager.getUserProfileStatus(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testGetUserProfileStatusWithNoPhone() {
        dataManager.save(clientTransferUserFirst);
        dataManager.updateUserPhoneByTelegramId(clientTransferUserFirst.tgUserId(), null);
        assertEquals(UserProfileStatus.UNCONFIRMED, dataManager.getUserProfileStatus(clientTransferUserFirst.tgUserId()));
    }

    @Test
    public void testGetUserProfileStatusWithNonExistentUser() {
        assertEquals(UserProfileStatus.GUEST, dataManager.getUserProfileStatus(99999L));
    }

    @Test
    public void testUpdate() {
        dataManager.save(clientTransferUserFirst);
        ClientTransfer updatedClientTransfer = new ClientTransfer(
                clientTransferUserFirst.id(),
                clientTransferUserFirst.tgUserId(),
                "1234567890",
                "Updated User",
                new Date(System.currentTimeMillis()),
                "vpn_profile_updated",
                false,
                new Date(System.currentTimeMillis())
        );
        dataManager.update(updatedClientTransfer);
        ClientTransfer foundClient = dataManager.findById(clientTransferUserFirst.tgUserId());
        assertNotNull(foundClient);
        assertEquals("Updated User", foundClient.name());
        assertFalse(foundClient.isVpnProfileAlive());
    }


}
