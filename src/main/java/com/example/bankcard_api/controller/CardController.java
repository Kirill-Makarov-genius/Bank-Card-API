package com.example.bankcard_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.bankcard_api.DTO.CardDTO;
import com.example.bankcard_api.DTO.CardCreateRequest;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.Role;
import com.example.bankcard_api.service.CardService;
import com.example.bankcard_api.service.UserService;
import com.example.bankcard_api.DTO.UpdateBalanceRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/cards")
public class CardController {
    
    private final UserService userService;
    private final CardService cardService;

    @GetMapping("/my")
    public ResponseEntity<Page<CardDTO>> gerAllCasrds(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication auth) {
        User currentUser = userService.getCurrentUser(auth);
        Page<CardDTO> cards = cardService.getUserCards(currentUser, page, size);

        if (cards.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }


    @PostMapping("/my")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardCreateRequest request, Authentication auth) {
        User currentUser = userService.getCurrentUser(auth);
        String cardNumber = request.getCardNumber();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(cardService.createCard(currentUser, cardNumber));
    }
    
    @PutMapping("/{cardId}/balance")
    public ResponseEntity<CardDTO> updateBalance(
        @PathVariable Long cardId,
        @RequestBody UpdateBalanceRequest request,
        Authentication auth
    ) {
        
        User currentUser = userService.getCurrentUser(auth);
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        BigDecimal newBalance = request.getBalance();
        return ResponseEntity.ok(cardService.updateBalance(cardId, newBalance, currentUser, isAdmin));
     
    }
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId, Authentication auth) {
        
        User currentUser = userService.getCurrentUser(auth);
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        System.out.println(isAdmin);
        System.out.println(Role.ADMIN);
        System.out.println(currentUser.getRole());

        cardService.deleteCard(cardId, currentUser, isAdmin);
        
        return ResponseEntity.noContent().build();

        
    }
    
    
     

}
