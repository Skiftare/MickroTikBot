package edu.Integrations.wallet.crypto.stellar;

import edu.Integrations.wallet.ctrypto.stellar.StellarConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

import static org.junit.jupiter.api.Assertions.*;

class StellarConnectionTest {

    private static final String TEST_PUBLIC_KEY = "GBXGIY4RRWFHV4ZQXBYYQXCZEZPID5X4HXVRDUZ7SXX2Z2JBQZ3MNWG6";
    private static final String TEST_SECRET_KEY = "SDZWG5BCXHLS3VFDOVZ2CQA7WVFQ4TWGWNTKM42BKCND3CFWWIIQ5JIF";

    @BeforeEach
    void setUp() {
        // Устанавливаем тестовые переменные окружения
        System.setProperty("STELLAR_PUBLIC_KEY", TEST_PUBLIC_KEY);
        System.setProperty("STELLAR_SECRET_KEY", TEST_SECRET_KEY);
        System.setProperty("STELLAR_NETWORK", "testnet");
    }

    @AfterEach
    void tearDown() {
        // Очищаем тестовые переменные окружения
        System.clearProperty("STELLAR_PUBLIC_KEY");
        System.clearProperty("STELLAR_SECRET_KEY");
        System.clearProperty("STELLAR_NETWORK");
    }

    @Test
    void testTestnetConnection() {
        System.setProperty("STELLAR_NETWORK", "testnet");
        StellarConnection connection = new StellarConnection();

        
        assertNotNull(connection.getServer());
        assertEquals(Network.TESTNET, connection.getNetwork());
    }

    @Test
    void testPublicNetworkConnection() {
        System.setProperty("STELLAR_NETWORK", "public");
        StellarConnection connection = new StellarConnection();
        
        assertNotNull(connection.getServer());
        assertEquals(Network.PUBLIC, connection.getNetwork());
    }

} 