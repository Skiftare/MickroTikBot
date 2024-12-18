package edu.Integrations.wallet.ctrypto.stellar;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import java.util.logging.Logger;

//Этот класс по идее не должен быть публичным - слишком много интересной информации в нём.
//Пользуемся им только для поднятия AccountListener.
public class StellarConnection {
    private static final String STELLAR_PUBLIC_KEY_ENV_NAME = "STELLAR_PUBLIC_KEY";
    private static final String STELLAR_SECRET_KEY_ENV_NAME = "STELLAR_SECRET_KEY";
    private static final String STELLAR_NETWORK_ENV_NAME = "STELLAR_NETWORK";
    public static String publicKey = System.getenv(STELLAR_PUBLIC_KEY_ENV_NAME);
    private static String secretKey = System.getenv(STELLAR_SECRET_KEY_ENV_NAME);
    private static String networkName = System.getenv(STELLAR_NETWORK_ENV_NAME);
    private final Server server;
    private final Network network;
    private KeyPair keyPair;  // добавляем KeyPair


    public StellarConnection() {
        publicKey = System.getenv(STELLAR_PUBLIC_KEY_ENV_NAME);
        secretKey = System.getenv(STELLAR_SECRET_KEY_ENV_NAME);
        networkName = System.getenv(STELLAR_NETWORK_ENV_NAME);
        if (networkName == null) {
            Logger.getAnonymousLogger().info("Network name is null");
            networkName = System.getProperty(STELLAR_NETWORK_ENV_NAME);
        }

        if (secretKey != null && !secretKey.isEmpty()) {
            keyPair = KeyPair.fromSecretSeed(secretKey.toCharArray());
        } else if (publicKey != null && !publicKey.isEmpty()) {
            keyPair = KeyPair.fromAccountId(publicKey);
        }

        if ("testnet".equals(networkName)) {
            network = Network.TESTNET;
            server = new Server("https://horizon-testnet.stellar.org/");
            Logger.getAnonymousLogger().info("Testnet connection established");
        } else {
            Logger.getAnonymousLogger().info("Public network connection established");
            Logger.getAnonymousLogger().info(networkName);
            network = Network.PUBLIC;
            server = new Server("https://horizon.stellar.org/");
        }
    }

    public Server getServer() {
        return server;
    }

    public Network getNetwork() {
        return network;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

}