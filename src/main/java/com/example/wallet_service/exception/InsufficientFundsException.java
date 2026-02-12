package com.example.wallet_service.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    private final UUID id;
    private final BigDecimal balance;
    private final BigDecimal requestedAmount;

    public InsufficientFundsException(UUID id, BigDecimal balance, BigDecimal requestedAmount) {
        super(String.format("Insufficient funds in wallet %s: balance %s, requested %s",
                id, balance, requestedAmount));
        this.id = id;
        this.balance = balance;
        this.requestedAmount = requestedAmount;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}
