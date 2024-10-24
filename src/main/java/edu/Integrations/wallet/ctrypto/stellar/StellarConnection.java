package edu.Integrations.wallet.ctrypto.stellar;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import org.stellar.sdk.KeyPair;

public class StellarConnection {
    private Server server;
    private Network network;


    public StellarConnection() {
        String publickKey = System.getenv("STELLAR_PUBLIC_KEY");
        String secretKey = System.getenv("STELLAR_SECRET_KEY");
        String networkName = System.getenv("STELLAR_NETWORK");
        if ("testnet".equals(networkName)) {
            network = Network.TESTNET;
            server = new Server("https://horizon-testnet.stellar.org");
        } else {
            network = Network.PUBLIC;
            server = new Server("https://horizon.stellar.org");
        }
    }

    public Server getServer() {
        return server;
    }

    public Network getNetwork() {
        return network;
    }

}
