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
    private static final String publicKey = System.getenv("STELLAR_PUBLIC_KEY");
    private static final String secretKey = System.getenv("STELLAR_SECRET_KEY");
    private static final String networkName = System.getenv("STELLAR_NETWORK");


    public StellarConnection() {

        Logger.getAnonymousLogger().info("Stellar connection initialisation");
        // Создаем KeyPair из секретного ключа
        Logger.getAnonymousLogger().info("Создаем KeyPair из секретного ключа");
        Logger.getAnonymousLogger().info("Секретный ключ: " + secretKey);
        Logger.getAnonymousLogger().info("Публичный ключ: " + publicKey);
        if (secretKey != null && !secretKey.isEmpty()) {
            keyPair = KeyPair.fromSecretSeed(secretKey.toCharArray());
            System.out.println("KeyPair created from secret key: " + keyPair.getSecretSeed());
        } else if (publicKey != null && !publicKey.isEmpty()) {
            keyPair = KeyPair.fromAccountId(publicKey);
            System.out.println("KeyPair created from public key: " + keyPair.getAccountId());
        }

        if ("testnet".equals(networkName)) {
            network = Network.TESTNET;
            String tt = "https://horizon-testnet.stellar.org/";
            server = new Server(tt);
            Logger.getAnonymousLogger().info("Testnet connection established");
            Logger.getAnonymousLogger().info("Testnet server: " + tt);
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
