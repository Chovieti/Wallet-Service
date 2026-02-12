package com.example.wallet_service.controller;

import com.example.wallet_service.dto.OperationRequest;
import com.example.wallet_service.service.WalletService;
import org.springframework.web.bind.annotation.*;

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
    public void walletOperation(@RequestBody OperationRequest request) {
        switch (request.operationType()) {
            case "DEPOSIT":
                // Метод пополнения кошелька
                service.deposit(request.walletId(), request.amount());
                break;
            case "WITHDRAW":
                // Метод снятия с кошелька
                service.withdraw(request.walletId(), request.amount());
                break;
            default:
                // Реализовать вывод ошибки
                break;
        }
    }

    // GET-method
    @GetMapping("/wallet/{id}")
    public void getWalletBalance(@PathVariable UUID id) {
        // Нужен метод для получения баланса с помощью сервиса
    }

}
