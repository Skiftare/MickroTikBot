package edu.Integrations.wallet.ctrypto.stellar;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import org.stellar.sdk.KeyPair;


import java.util.logging.Logger;

//Этот класс по идее не должен быть публичным - слишком много интересной информации в нём.
//Пользуемся им только для поднятия AccountListener.
public class StellarConnection {
    private Server server;
    private Network network;
    private KeyPair keyPair;  // добавляем KeyPair
    static final String publicKey = System.getenv("STELLAR_PUBLIC_KEY");
    private static final String secretKey = System.getenv("STELLAR_SECRET_KEY");
    private static final String networkName = System.getenv("STELLAR_NETWORK");


    public StellarConnection() {

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
