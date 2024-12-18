package edu.Data.dto;

import java.math.BigDecimal;

public record TransactionRecord(
        String paymentHash,
        String paymentKey,
        BigDecimal amount,
        String sourceAccount
) {
}
