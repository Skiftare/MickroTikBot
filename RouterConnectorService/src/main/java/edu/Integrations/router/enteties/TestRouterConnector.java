package edu.Integrations.router.enteties;


import edu.Integrations.router.VpnProfileServerManager;
import edu.dto.ClientDtoToRouter;
import edu.dto.ClientDtoToRouterWithVpnProfile;

public class TestRouterConnector implements VpnProfileServerManager {


    private static String generateSuccessMessageForUser() {
        String res = "VPN профиль успешно создан!\n"
                + "Адрес VPN-сервера: " + "localhost" + "\n"
                + "\nLogin for l2tp: " + "testlogin"
                + "\n\nPassword for l2tp: " + "testpassword"
                + "\n\nSecret: vpn";
        return res;
    }

    public String initialisationSecret(ClientDtoToRouter clientTransfer) {
        return generateSuccessMessageForUser();
    }

    public String initialisationTrial(ClientDtoToRouter clientTransfer) {
        return generateSuccessMessageForUser();
    }

    public String prolongSecret(ClientDtoToRouterWithVpnProfile clientTransfer) {
        return generateSuccessMessageForUser();
    }

    @Override
    public String establishingSSH() {
        return "SSH is ok";
    }


}
