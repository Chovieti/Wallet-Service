package com.example.wallet_service.controller;

import com.example.wallet_service.dto.BalanceResponse;
import com.example.wallet_service.dto.OperationRequest;
import com.example.wallet_service.exception.InvalidOperationTypeException;
import com.example.wallet_service.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class WalletController {
    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    // POST-method
    @PostMapping("/wallet")
    public ResponseEntity<Void> walletOperation(@Valid @RequestBody OperationRequest request) {
        switch (request.operationType()) {
            case "DEPOSIT":
                service.deposit(request.walletId(), request.amount());
                break;
            case "WITHDRAW":
                service.withdraw(request.walletId(), request.amount());
                break;
            default:
                throw new InvalidOperationTypeException(request.operationType());
        }
        return ResponseEntity.ok().build();
    }

    // GET-method
    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<BalanceResponse> getWalletBalance(@PathVariable("WALLET_UUID") UUID id) {
        BigDecimal balance = service.getBalance(id);
        return ResponseEntity.ok(new BalanceResponse(balance));
    }

}
