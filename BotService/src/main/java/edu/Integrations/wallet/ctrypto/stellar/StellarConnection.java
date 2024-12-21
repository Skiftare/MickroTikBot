package edu.Integrations.wallet.ctrypto.stellar;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import java.util.logging.Logger;

//Этот класс по идее не должен быть публичным - слишком много интересной информации в нём.
//Пользуемся им только для поднятия AccountListener.
public class StellarConnection {
    public final String publicKey;
    private final Server server;
    private final Network network;
    private KeyPair keyPair;  // добавляем KeyPair


    public StellarConnection(String incomePublicKey, String incomeSecretKey, String incomeNetworkName) {

        this.publicKey = incomePublicKey;
        Logger.getAnonymousLogger().info("Stellar connection establishing");
        Logger.getAnonymousLogger().info("Public key is " + publicKey);
        Logger.getAnonymousLogger().info("Network name is " + incomeNetworkName);


        if (incomeSecretKey != null && !incomeSecretKey.isEmpty()) {
            keyPair = KeyPair.fromSecretSeed(incomeSecretKey.toCharArray());
        } else if (publicKey != null && !publicKey.isEmpty()) {
            keyPair = KeyPair.fromAccountId(publicKey);
        }

        if ("testnet".equals(incomeNetworkName)) {
            network = Network.TESTNET;
            server = new Server("https://horizon-testnet.stellar.org/");
            Logger.getAnonymousLogger().info("Testnet connection established");
        } else {
            Logger.getAnonymousLogger().info("Public network connection established");
            Logger.getAnonymousLogger().info(incomeNetworkName);
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
