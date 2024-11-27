package edu.Data.dto;

public record PaymentRecord(
        Long paymentId,
        String comment,
        Boolean status,
        Long amount
) {
}
