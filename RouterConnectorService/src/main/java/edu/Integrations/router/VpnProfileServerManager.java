package edu.Integrations.router;

import edu.dto.ClientDtoToRouter;
import edu.dto.ClientDtoToRouterWithVpnProfile;

public interface VpnProfileServerManager {
    public static String initialisationSecret(ClientDtoToRouter clientTransfer) {
        return null;
    }

    public static String initialisationTrial(ClientDtoToRouter clientTransfer) {
        return null;
    }

    public static String prolongSecret(ClientDtoToRouterWithVpnProfile clientTransfer) {
        return null;
    }
}
