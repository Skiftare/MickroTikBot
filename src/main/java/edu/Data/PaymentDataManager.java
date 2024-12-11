package edu.Data;

import org.stellar.sdk.responses.operations.PaymentOperationResponse;

/**
 * Интерфейс для управления платежными данными.
 * Предоставляет методы для обработки входящих транзакций.
 */
public interface PaymentDataManager {

    /**
     * Добавляет входящую транзакцию в систему.
     *
     * @param paymentOperation информация о платежной операции
     */
    void addIncomingTransaction(PaymentOperationResponse paymentOperation);
}
