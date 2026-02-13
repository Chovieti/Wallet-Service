package com.example.wallet_service.exception;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {
    private final UUID id;

    public WalletNotFoundException(UUID id) {
        super("Wallet with id " + id + " not found");
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
