package edu.Data;

import edu.Configuration.DataConnectConfigurator;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public interface PaymentDataManager {




    void addIncomingTransaction(PaymentOperationResponse paymentOperation);

    
}
