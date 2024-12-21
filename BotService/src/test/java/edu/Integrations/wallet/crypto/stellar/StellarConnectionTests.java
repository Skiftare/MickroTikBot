package edu.Integrations.wallet.crypto.stellar;

import edu.Integrations.wallet.ctrypto.stellar.StellarConnection;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.stellar.sdk.Network;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class StellarConnectionTests {

    private static final String TEST_PUBLIC_KEY = "GC3BZKAA7ZJWOLRFRET5SG62VDM4PXGPL2K26QNYONYAOTMNJ37OUP25";
    private static final String TEST_SECRET_KEY = "SD6QCGSODEMALMKSCBKDYHA56XFRAL62R45XDCPBDPC3EEUDSVLRT64S";


    @Nested
    class TestsAtRealNetwork {

        @Test
        public void testBothKeysAndPublicNetwork() throws Exception {


            StellarConnection connection = new StellarConnection(TEST_PUBLIC_KEY, TEST_SECRET_KEY, "public");

            assertNotNull(connection.getServer());
            assertEquals(Network.PUBLIC, connection.getNetwork());
            assertEquals(TEST_PUBLIC_KEY, connection.getKeyPair().getAccountId());
        }

        @Test
        public void testOnlyPublicKeyAndPublicNetwork() throws Exception {


            StellarConnection connection = new StellarConnection(TEST_PUBLIC_KEY, "", "public");

            assertNotNull(connection.getServer());
            assertEquals(Network.PUBLIC, connection.getNetwork());
            assertEquals(TEST_PUBLIC_KEY, connection.getKeyPair().getAccountId());
        }

        @Test
        public void testNoKeysAndPublicNetwork() {


            StellarConnection connection = new StellarConnection("", "", "public");

            assertNotNull(connection.getServer());
            assertEquals(Network.PUBLIC, connection.getNetwork());
            assertNull(connection.getKeyPair());
        }
    }

    @Nested
    class TestsAtTestNetwork {

        @Test
        public void testBothKeysAndTestnet() throws Exception {


            StellarConnection connection = new StellarConnection(TEST_PUBLIC_KEY, TEST_SECRET_KEY, "testnet");

            assertNotNull(connection.getServer());
            assertEquals(Network.TESTNET, connection.getNetwork());
            assertEquals(TEST_PUBLIC_KEY, connection.getKeyPair().getAccountId());
        }

        @Test
        public void testOnlyPublicKeyAndTestnet() {


            StellarConnection connection = new StellarConnection(TEST_PUBLIC_KEY, "", "testnet");

            assertNotNull(connection.getServer());
            assertEquals(Network.TESTNET, connection.getNetwork());
            assertEquals(TEST_PUBLIC_KEY, connection.getKeyPair().getAccountId());
        }

        @Test
        public void testNoKeysAndTestnet() {


            StellarConnection connection = new StellarConnection("", "", "testnet");

            assertNotNull(connection.getServer());
            assertEquals(Network.TESTNET, connection.getNetwork());
            assertNull(connection.getKeyPair());
        }

    }
}

