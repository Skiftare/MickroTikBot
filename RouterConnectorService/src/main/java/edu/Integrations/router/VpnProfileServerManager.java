package edu.Integrations.router;

import edu.dto.ClientDtoToRouter;
import edu.dto.ClientDtoToRouterWithVpnProfile;

public interface VpnProfileServerManager {
    String initialisationSecret(ClientDtoToRouter clientTransfer);

    String initialisationTrial(ClientDtoToRouter clientTransfer);

    String prolongSecret(ClientDtoToRouterWithVpnProfile clientTransfer);

    String establishingSSH();
}
