package com.example.wallet_service.repository;

import com.example.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends CrudRepository<Wallet, UUID> {
    // Метод для получения баланса
    @Query("SELECT w.balance FROM Wallet w WHERE w.id = :id")
    Optional<BigDecimal> findBalanceById(UUID id);
    // Метод для пополнения кошелька
    @Modifying
    @Query("UPDATE Wallet w SET w.balance = w.balance + :amount WHERE w.id = :id")
    int depositOperation(UUID id, BigDecimal amount);
    // Метод для снятия с кошелька
    @Modifying
    @Query("UPDATE Wallet w SET w.balance = w.balance - :amount WHERE w.id = :id AND w.balance >= :amount")
    int withdrawOperation(UUID id, BigDecimal amount);
}