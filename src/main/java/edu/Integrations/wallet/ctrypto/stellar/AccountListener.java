package edu.Integrations.wallet.ctrypto.stellar;

import edu.Data.PaymentDataManager;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

public class AccountListener {

    private StellarConnection connection;
    private Consumer<PaymentInfo> paymentProcessor;

    public AccountListener(Consumer<PaymentInfo> paymentProcessor) {
        this.connection = new StellarConnection();
        this.paymentProcessor = paymentProcessor;
    }

    public void listenToAccount(String accountId) {
        connection.getServer().operations().forAccount(accountId).stream(new EventListener<OperationResponse>() {
            @Override
            public void onEvent(OperationResponse operation) {
                if (operation instanceof PaymentOperationResponse) {
                    PaymentOperationResponse payment = (PaymentOperationResponse) operation;
                    paymentProcessor.accept(new PaymentInfo(
                        payment.getFrom(),
                        payment.getTo(),
                        payment.getTransaction().get().getMemo().toString(),
                        new BigDecimal(payment.getAmount()),
                        payment.getAsset().getType()
                    ));
                    String memo = payment.getTransaction().get().getMemo().toString();
                    PaymentDataManager.changeStatus(memo);
                }
            }

            @Override
            public void onFailure(Optional<Throwable> error, Optional<Integer> responseCode) {
                System.out.println("Не удалось получить информацию о платеже: " + error.orElse(null));
            }
        });
    }

    public static class PaymentInfo {
        public final String from;
        public final String to;
        public final String memo;
        public final BigDecimal amount;
        public final String asset;

        public PaymentInfo(String from, String to, String memo, BigDecimal amount, String asset) {
            this.from = from;
            this.to = to;
            this.memo = memo;
            this.amount = amount;
            this.asset = asset;
        }
    }
}
