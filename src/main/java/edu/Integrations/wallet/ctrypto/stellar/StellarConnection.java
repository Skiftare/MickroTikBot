package edu.Integrations.wallet.ctrypto.stellar;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

import java.util.logging.Logger;

//Этот класс по идее не должен быть публичным - слишком много интересной информации в нём.
//Пользуемся им только для поднятия AccountListener.
public class StellarConnection {
    public static final String STELLAR_PUBLIC_KEY = System.getenv("STELLAR_PUBLIC_KEY");
    private static final String STELLAR_SECRET_KEY = System.getenv("STELLAR_SECRET_KEY");
    private static final String STELLAR_NETWORK = System.getenv("STELLAR_NETWORK");
    private final Server server;
    private final Network network;
    private KeyPair keyPair;  // добавляем KeyPair


    public StellarConnection() {

        if (STELLAR_SECRET_KEY != null && !STELLAR_SECRET_KEY.isEmpty()) {
            keyPair = KeyPair.fromSecretSeed(STELLAR_SECRET_KEY.toCharArray());
        } else if (STELLAR_PUBLIC_KEY != null && !STELLAR_PUBLIC_KEY.isEmpty()) {
            keyPair = KeyPair.fromAccountId(STELLAR_PUBLIC_KEY);
        }

        if ("testnet".equals(STELLAR_NETWORK)) {
            network = Network.TESTNET;
            server = new Server("https://horizon-testnet.stellar.org/");
            Logger.getAnonymousLogger().info("Testnet connection established");
        } else {
            network = Network.PUBLIC;
            server = new Server("https://horizon.stellar.org/");
        }
    }

    Server getServer() {
        return server;
    }

    Network getNetwork() {
        return network;
    }

    KeyPair getKeyPair() {
        return keyPair;
    }

}
