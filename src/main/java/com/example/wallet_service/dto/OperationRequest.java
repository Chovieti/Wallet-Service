package com.example.wallet_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OperationRequest (
    @NotNull UUID walletId,

    @NotNull String operationType,

    @NotNull @Positive BigDecimal amount
) {}
