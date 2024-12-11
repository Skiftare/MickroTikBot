package edu.Integrations.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;

@ExtendWith(MockitoExtension.class)
class CryptoGeneratorTest {

    @BeforeAll
    static void setUp() {
        System.setProperty("SALT", "test_salt");
        System.setProperty("KEY_FOR_AES", "test_key_16_bytes!");
    }

    @Test
    void testGenerateCheckSum() {
        Long userId = 123456L;
        String checkSum1 = CryptoGenerator.generateCheckSum(userId);
        String checkSum2 = CryptoGenerator.generateCheckSum(userId);

        assertNotNull(checkSum1);
        assertNotNull(checkSum2);
        assertNotEquals(checkSum1, checkSum2);
        assertEquals(27, checkSum1.length());
        assertEquals(27, checkSum2.length());
    }

    @Test
    void testGenerateUsersHash() {
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            123456L,
            "+1234567890",
            UserProfileStatus.ACTIVE_VPN,
            "Test User",
            "Test LastName"
        );

        String hash1 = CryptoGenerator.generateUsersHash(wrapper);
        String hash2 = CryptoGenerator.generateUsersHash(wrapper);

        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(hash1, hash2);
        assertTrue(hash1.length() <= 27);
        assertTrue(hash2.length() <= 27);
    }

    @Test
    void testGenerateCheckSumWithDifferentUserIds() {
        Long userId1 = 123456L;
        Long userId2 = 789012L;

        String checkSum1 = CryptoGenerator.generateCheckSum(userId1);
        String checkSum2 = CryptoGenerator.generateCheckSum(userId2);

        assertNotNull(checkSum1);
        assertNotNull(checkSum2);
        assertNotEquals(checkSum1, checkSum2);
    }

    @Test
    void testGenerateUsersHashWithSameInput() {
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            123456L,
            "+1234567890",
            UserProfileStatus.ACTIVE_VPN,
            "Test User",
            "Test LastName"
        );

        String hash1 = CryptoGenerator.generateUsersHash(wrapper);
        String hash2 = CryptoGenerator.generateUsersHash(wrapper);
        String hash3 = CryptoGenerator.generateUsersHash(wrapper);

        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);

    }

    @Test
    void testCheckSumFormat() {
        Long userId = 123456L;
        String checkSum = CryptoGenerator.generateCheckSum(userId);

        assertTrue(checkSum.matches("[a-f0-9]+"));
    }

    @Test
    void testGenerateUsersHashWithNullValues() {
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            null,
            null,
            UserProfileStatus.UNCONFIRMED,
            null,
            null
        );

        assertDoesNotThrow(() -> {
            String hash = CryptoGenerator.generateUsersHash(wrapper);
            assertNotNull(hash);
            assertTrue(hash.length() <= 27);
        });
    }
}