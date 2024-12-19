package edu.Integrations.router;

import edu.Integrations.router.enteties.RouterConnector;
import edu.Integrations.router.enteties.TestRouterConnector;
import edu.dto.ClientDtoToRouter;
import edu.dto.ClientDtoToRouterWithVpnProfile;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

public class VpnProfileServerManagerFactory implements VpnProfileServerManager {
    private static Map<String, VpnProfileServerManager> managerTable = new HashMap<>();
    private static String envForRouterConnectorBehaviour = System.getenv("ROUTER_BEHAVIOUR");
    private static final String DEFAULT_ENV = "test";

    public VpnProfileServerManagerFactory() {
        if (envForRouterConnectorBehaviour == null) {
            envForRouterConnectorBehaviour = DEFAULT_ENV;
        }
        managerTable.put("production", new RouterConnector());
        managerTable.put(DEFAULT_ENV, new TestRouterConnector());
    }

    @Override
    public String initialisationSecret(ClientDtoToRouter clientTransfer) {
        Logger.getAnonymousLogger().info("Doing response to initialisationSecret with clientTransfer: "
                + clientTransfer);
        Logger.getAnonymousLogger().info("envForRouterConnectorBehaviour: "
                + envForRouterConnectorBehaviour);
        return managerTable.get(envForRouterConnectorBehaviour).initialisationSecret(clientTransfer);
    }

    @Override
    public String initialisationTrial(ClientDtoToRouter clientTransfer) {
        return managerTable.get(envForRouterConnectorBehaviour).initialisationTrial(clientTransfer);
    }

    @Override
    public String prolongSecret(ClientDtoToRouterWithVpnProfile clientTransfer) {
        return managerTable.get(envForRouterConnectorBehaviour).prolongSecret(clientTransfer);
    }

}
