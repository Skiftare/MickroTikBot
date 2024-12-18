package edu.Integrations.router;


import edu.Data.dto.ClientTransfer;

public class TestRouterConnector {
    private static String generateSuccessMessageForUser() {
        String res =  "VPN профиль успешно создан!\n"
                + "Адрес VPN-сервера: " + "localhost" + "\n"
                + "\nLogin for l2tp: " + "test"
                + "\n\nPassword for l2tp: " + "test"
                + "\n\nSecret: vpn";
        return res;
    }
    static String initialisationSecret(ClientTransfer clientTransfer) {
        return generateSuccessMessageForUser();
    }
    static String initialisationTrial(ClientTransfer clientTransfer) {
        return generateSuccessMessageForUser();
    }
    static String prolongSecret(ClientTransfer clientTransfer) {
        return generateSuccessMessageForUser();
    }


}
