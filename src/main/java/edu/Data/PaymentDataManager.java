package edu.Data;

import org.stellar.sdk.responses.operations.PaymentOperationResponse;

public interface PaymentDataManager {


    void addIncomingTransaction(PaymentOperationResponse paymentOperation);


}
