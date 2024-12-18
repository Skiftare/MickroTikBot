package edu.Integrations.router;

import edu.Integrations.router.enteties.RouterConnector;
import edu.Integrations.router.enteties.TestRouterConnector;
import edu.dto.ClientDtoToRouter;
import edu.dto.ClientDtoToRouterWithVpnProfile;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class VpnProfileServerManagerFactory implements VpnProfileServerManager {
    private static final Map<String, VpnProfileServerManager> managerTable = new HashMap<>();
    private static String envForRouterConnectorBehaviour = System.getenv("ROUTER_BEHAVIOUR");
    private static final String DEFAULT_ENV = "test";

    static {
        managerTable.put("production", RouterConnector.getInstance());
        managerTable.put("test", TestRouterConnector.getInstance());
    }

    @Override
    public String initialisationSecret(ClientDtoToRouter clientTransfer) {
        return managerTable.get(envForRouterConnectorBehaviour).initialisationSecret(clientTransfer);
        //return invokeDynamic("initialisationSecret", new Class[]{ClientDtoToRouter.class}, new Object[]{clientTransfer});
    }

    @Override
    public String initialisationTrial(ClientDtoToRouter clientTransfer) {
        return managerTable.get(envForRouterConnectorBehaviour).initialisationTrial(clientTransfer);
        //return invokeDynamic("initialisationTrial", new Class[]{ClientDtoToRouter.class}, new Object[]{clientTransfer});
    }

    @Override
    public String prolongSecret(ClientDtoToRouterWithVpnProfile clientTransfer) {
        return managerTable.get(envForRouterConnectorBehaviour).prolongSecret(clientTransfer);
        //return invokeDynamic("prolongSecret", new Class[]{ClientDtoToRouterWithVpnProfile.class}, new Object[]{clientTransfer});
    }

    private String invokeDynamic(String methodName, Class<?>[] paramTypes, Object[] params) {
        try {
            // Получаем реализацию в зависимости от текущей среды
            if (envForRouterConnectorBehaviour == null || !managerTable.containsKey(envForRouterConnectorBehaviour)) {
                envForRouterConnectorBehaviour = DEFAULT_ENV;
            }

            VpnProfileServerManager manager = managerTable.get(envForRouterConnectorBehaviour);

            // Используем рефлексию для вызова метода
            Method method = manager.getClass().getMethod(methodName, paramTypes);
            return (String) method.invoke(manager, params);
        } catch (Exception e) {
            Logger.getAnonymousLogger().info(e.getMessage());
            return "Error occurred during dynamic invocation";
        }
    }
}
