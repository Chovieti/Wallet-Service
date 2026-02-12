package com.example.wallet_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Wallet {
    @Id
    private UUID id;
    private BigDecimal balance;

    protected Wallet() {}

    public Wallet(UUID id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

    public Wallet(UUID id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
