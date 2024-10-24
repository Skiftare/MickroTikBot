package edu.Integrations.wallet.ctrypto.stellar;

import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.util.Optional;

public class AccountListener {

    private StellarConnection connection;

    public AccountListener() {
        this.connection = new StellarConnection();
    }

    public void listenToAccount(String accountId) {
        connection.getServer().operations().forAccount(accountId).stream(new EventListener<OperationResponse>() {
            @Override
            public void onEvent(OperationResponse operation) {
                if (operation instanceof PaymentOperationResponse) {
                    PaymentOperationResponse payment = (PaymentOperationResponse) operation;
                    System.out.println("New payment received!");
                    System.out.println("From: " + payment.getFrom());
                    System.out.println("To: " + payment.getTo());
                    System.out.println("Phase: "+payment.getTransaction().get().getMemo());
                    System.out.println("Amount: " + payment.getAmount());
                    System.out.println("Asset: " + payment.getAsset());
                }
            }

            @Override
            public void onFailure(Optional<Throwable> error, Optional<Integer> responseCode) {
                System.out.println("Failed to retrieve payment info: " + error.orElse(null));
            }
        });
    }
}
