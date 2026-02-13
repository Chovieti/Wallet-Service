package com.example.wallet_service;

import com.example.wallet_service.dto.OperationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("wallet_test")
@Transactional
public class WalletControllerTest {
    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;
    private static final String WALLET_ID = "22222222-2222-2222-2222-222222222222";
    private static final String WRONG_WALLET_ID = "21098765-4321-0987-6543-210987654321";
    
    // GET-запрос
    @Test
    void getBalanceShouldShowBalance() throws Exception {
        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }
    
    @Test
    void getBalanceShouldFailWalletNotFound() throws Exception {
        mock.perform(get(String.format("/api/v1/wallets/%s", WRONG_WALLET_ID)))
                .andExpect(status().isNotFound());
    }

    // POST-запрос DEPOSIT
    @Test
    void depositShouldIncreaseBalance() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "DEPOSIT",
                new BigDecimal("50.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1050.00));
    }

    @Test
    void depositShouldFailWalletNotFound() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WRONG_WALLET_ID),
                "DEPOSIT",
                new BigDecimal("50.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void depositShouldFailValidation() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "DEPOSIT",
                new BigDecimal("-100.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void depositShouldFailAmountIsZero() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "DEPOSIT",
                new BigDecimal("0.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }
    
    // POST-запрос WITHDRAW
    @Test
    void withdrawShouldDecreaseBalance() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "WITHDRAW",
                new BigDecimal("50.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(950.00));
    }

    @Test
    void withdrawShouldFailWalletNotFound() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WRONG_WALLET_ID),
                "WITHDRAW",
                new BigDecimal("50.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void withdrawShouldFailNotEnoughMoney() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "WITHDRAW",
                new BigDecimal("500000.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void withdrawShouldFailAmountIsNegative() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "WITHDRAW",
                new BigDecimal("-100.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void shouldFailWrongOperationType() throws Exception {
        OperationRequest request = new OperationRequest(
                UUID.fromString(WALLET_ID),
                "error",
                new BigDecimal("50.00"));
        String json = mapper.writeValueAsString(request);

        mock.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        mock.perform(get(String.format("/api/v1/wallets/%s", WALLET_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }
}
