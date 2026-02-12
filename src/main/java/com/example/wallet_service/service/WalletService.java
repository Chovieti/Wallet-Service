package com.example.wallet_service.service;

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
        // TODO заменить на исключение (баланс не изменился, так как кошелек не существует)
        if (repository.depositOperation(id, amount) == 0) return;
    }

    @Transactional
    public void withdraw(UUID id, BigDecimal amount) {
        // TODO заменить на исключение (кошелек не существует)
        if (!repository.existsById(id)) return;
        // TODO заменить на исключение (недостаточно средств на кошельке)
        if (repository.withdrawOperation(id, amount) == 0) return;
    }

    public BigDecimal getBalance(UUID id) {
        Optional<BigDecimal> balance = repository.findBalanceById(id);
        // TODO заменить на исключение (кошелек не существует)
        return balance.orElse(BigDecimal.ZERO);
    }
}
