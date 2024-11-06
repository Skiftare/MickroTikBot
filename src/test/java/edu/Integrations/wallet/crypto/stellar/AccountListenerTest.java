package edu.Integrations.wallet.crypto.stellar;

import edu.Data.PaymentDataManager;
import edu.Integrations.wallet.ctrypto.stellar.AccountListener;
import edu.Integrations.wallet.ctrypto.stellar.StellarConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.requests.OperationsRequestBuilder;
import org.stellar.sdk.responses.TransactionResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountListenerTest {

    @Mock
    private StellarConnection stellarConnection;

    @Mock
    private PaymentDataManager paymentDataManager;

    @Mock
    private Server server;

    @Mock
    private PaymentsRequestBuilder paymentsRequestBuilder;

    @Mock
    private KeyPair keyPair;

    @Mock
    private OperationsRequestBuilder operationsRequestBuilder;

    private AccountListener accountListener;

    @BeforeEach
    void setUp() {
        when(stellarConnection.getServer()).thenReturn(server);
        when(stellarConnection.getKeyPair()).thenReturn(keyPair);
        when(server.operations()).thenReturn(operationsRequestBuilder);
        accountListener = new AccountListener(stellarConnection, paymentDataManager);
    }

    @Test
    void testSuccessfulPaymentProcessing() {

        PaymentOperationResponse mockPayment = mock(PaymentOperationResponse.class);
        org.stellar.sdk.Transaction mockTransaction = mock(org.stellar.sdk.Transaction.class);
        org.stellar.sdk.Memo mockMemo = mock(org.stellar.sdk.Memo.class);

        lenient().when(keyPair.getAccountId()).thenReturn("destination-account");
        lenient().when(mockPayment.getSourceAccount()).thenReturn("source-account");
        lenient().when(mockPayment.getTo()).thenReturn("destination-account");
        lenient().when(mockPayment.getAmount()).thenReturn("100.0000000");
        lenient().when(mockTransaction.getMemo()).thenReturn(mockMemo);

        lenient().when(operationsRequestBuilder.forAccount(anyString())).thenReturn(operationsRequestBuilder);
        lenient().when(operationsRequestBuilder.includeTransactions(anyBoolean())).thenReturn(operationsRequestBuilder);
        lenient().when(operationsRequestBuilder.cursor(anyString())).thenReturn(operationsRequestBuilder);
        TransactionResponse mockTransactionResponse = mock(TransactionResponse.class);

        lenient().when(mockPayment.getTransaction()).thenReturn(Optional.of(mockTransactionResponse));
        lenient().when(mockTransactionResponse.isSuccessful()).thenReturn(true);
        lenient().when(mockTransactionResponse.getMemo()).thenReturn(mockMemo);

        doAnswer(invocation -> {
            EventListener<OperationResponse> listener = invocation.getArgument(0);
            listener.onEvent(mockPayment);
            return null;
        }).when(operationsRequestBuilder).stream(any());

        accountListener.startListening();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(paymentDataManager).addIncomingTransaction(mockPayment);
    }

    @Test
    void testNonPaymentOperation() {
        OperationResponse mockOperation = mock(OperationResponse.class);

        lenient().doAnswer(invocation -> {
            EventListener<OperationResponse> listener = invocation.getArgument(0);
            listener.onEvent(mockOperation);
            return null;
        }).when(operationsRequestBuilder).stream(any());

        accountListener.startListening();

        verify(paymentDataManager, never()).addIncomingTransaction(any());
    }

}