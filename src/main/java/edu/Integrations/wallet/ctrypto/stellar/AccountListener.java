package edu.Integrations.wallet.ctrypto.stellar;

import edu.Data.PaymentDataManager;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Logger;


public class AccountListener {

    private final StellarConnection connection;
    private final PaymentDataManager paymentDataManager;

    public AccountListener(StellarConnection connection, PaymentDataManager paymentDataManager) {
        this.connection = connection;
        this.paymentDataManager = paymentDataManager;

    }

    public void startListening() {
        KeyPair keyPair = connection.getKeyPair();
        if (keyPair == null) {
            throw new IllegalStateException("Не настроены ключи Stellar.");
        }

        String accountId = keyPair.getAccountId();
        Logger.getAnonymousLogger().info("Начинаем прослушивание аккаунта: " + accountId);

        try {
            connection
                    .getServer()
                    .operations()
                    .forAccount(accountId)
                    .includeTransactions(true)

                    .stream(new EventListener<OperationResponse>() {
                                @Override
                                public void onEvent(OperationResponse operation) {
                                    // Обработка каждого события транзакции
                                    Logger.getAnonymousLogger().info("Новая транзакция обнаружена!");
                                    Logger.getAnonymousLogger().info("Тип операции: "
                                            + operation.getClass().getSimpleName());
                                    Logger.getAnonymousLogger().info("ID транзакции: "
                                            + operation.getTransactionHash());


                                    if (operation instanceof PaymentOperationResponse) {


                                        Logger.getAnonymousLogger().info("Получена новая входящая транзакция!");
                                        Logger.getAnonymousLogger().info("Источник: "
                                                + operation.getSourceAccount());
                                        Logger.getAnonymousLogger().info("Memo: "
                                                + operation.getTransaction().get().getMemo());
                                        if (operation.getTransaction().get().isSuccessful()) {
                                            BigDecimal amount = new BigDecimal(((PaymentOperationResponse) operation)
                                                    .getAmount());
                                            Logger.getAnonymousLogger().info("Сумма: " + amount);
                                            paymentDataManager
                                                    .addIncomingTransaction((PaymentOperationResponse) operation);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Optional<Throwable> optional, Optional<Integer> optional1) {
                                    if (optional instanceof Optional) {
                                        Logger.getAnonymousLogger().severe("Ошибка при обработке транзакции: "
                                                + optional.get().getMessage());
                                    } else {
                                        Logger.getAnonymousLogger().severe("Случилось что-то страшное "
                                                + optional1);
                                    }
                                }
                            }
                    );
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Ошибка при настройке прослушивания: " + e.getMessage());
        }
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
