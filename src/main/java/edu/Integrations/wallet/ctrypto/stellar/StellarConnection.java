package edu.Integrations.wallet.ctrypto.stellar;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

import java.util.logging.Logger;

//Этот класс по идее не должен быть публичным - слишком много интересной информации в нём.
//Пользуемся им только для поднятия AccountListener.
public class StellarConnection {

    private final Server server;
    private final Network network;
    private KeyPair keyPair;  // добавляем KeyPair


    public StellarConnection() {

        String stellarPublicKey = System.getenv("STELLAR_PUBLIC_KEY");
        String stellarSecretKey = System.getenv("STELLAR_SECRET_KEY");
        String stellarNetwork = System.getenv("STELLAR_NETWORK");

        if (stellarSecretKey != null && !stellarSecretKey.isEmpty()) {
            keyPair = KeyPair.fromSecretSeed(stellarSecretKey.toCharArray());
        } else if (stellarPublicKey != null && !stellarPublicKey.isEmpty()) {
            keyPair = KeyPair.fromAccountId(stellarPublicKey);
        }
        Logger.getAnonymousLogger().info("Stellar network: " + stellarNetwork);
        if ("testnet".equals(stellarNetwork)) {
            network = Network.TESTNET;
            server = new Server("https://horizon-testnet.stellar.org/");
            Logger.getAnonymousLogger().info("Testnet connection established");
        } else {
            Logger.getAnonymousLogger().info("Public network connection established");
            Logger.getAnonymousLogger().info(stellarNetwork);
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
