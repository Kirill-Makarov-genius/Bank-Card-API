package com.example.bankcard_api.controller;

import com.example.bankcard_api.DTO.TransferRequest;
import com.example.bankcard_api.DTO.TransferResponse;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.service.CardService;
import com.example.bankcard_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfers")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final CardService cardService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Transfer funds between cards")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request, Authentication auth) {
        User current = userService.getCurrentUser(auth);
        TransferResponse result = cardService.transfer(current, request.getFromCardNumber(), request.getToCardNumber(), request.getAmount());
        return ResponseEntity.ok(result);
    }
}
