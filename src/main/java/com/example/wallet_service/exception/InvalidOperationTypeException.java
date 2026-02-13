package com.example.wallet_service.exception;

public class InvalidOperationTypeException extends RuntimeException {
    private final String operationType;

    public InvalidOperationTypeException(String operationType) {
        super("Unsupported operation type: " + operationType + ". Allowed: DEPOSIT, WITHDRAW");
        this.operationType = operationType;
    }

    public String getOperationType() {
        return operationType;
    }
}
