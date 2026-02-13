package com.example.wallet_service.service;

import com.example.wallet_service.exception.InsufficientFundsException;
import com.example.wallet_service.exception.WalletNotFoundException;
import com.example.wallet_service.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void deposit(UUID id, BigDecimal amount) {
        if (repository.depositOperation(id, amount) == 0) throw new WalletNotFoundException(id);
    }

    @Transactional
    public void withdraw(UUID id, BigDecimal amount) {
        if (repository.withdrawOperation(id, amount) == 0) {
            BigDecimal balance = repository.findBalanceById(id).orElseThrow(() -> new WalletNotFoundException(id));
            throw new InsufficientFundsException(id, balance, amount);
        }
    }

    public BigDecimal getBalance(UUID id) {
        Optional<BigDecimal> balance = repository.findBalanceById(id);
        return balance.orElseThrow(() -> new WalletNotFoundException(id));
    }
}
