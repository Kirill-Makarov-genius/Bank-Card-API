package com.example.bankcard_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcard_api.DTO.AdminCardDTO;
import com.example.bankcard_api.DTO.AdminCreateCardRequest;
import com.example.bankcard_api.DTO.AdminUserDTO;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.CardStatus;
import com.example.bankcard_api.service.CardService;
import com.example.bankcard_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CardService cardService;
    private final UserService userService;

    // Cards
    @PostMapping("/cards")
    @Operation(summary = "Create card for a user")
    public ResponseEntity<AdminCardDTO> createCard(@Valid @RequestBody AdminCreateCardRequest request) {
        AdminCardDTO dto = cardService.createCardForUser(request.getUserId(), request.getCardNumber(), request.getExpiry());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/cards/{cardId}/block")
    @Operation(summary = "Block a card")
    public ResponseEntity<AdminCardDTO> blockCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.setCardStatus(cardId, CardStatus.BLOCKED));
    }

    @PatchMapping("/cards/{cardId}/activate")
    @Operation(summary = "Activate a card")
    public ResponseEntity<AdminCardDTO> activateCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.setCardStatus(cardId, CardStatus.ACTIVE));
    }

    @DeleteMapping("/cards/{cardId}")
    @Operation(summary = "Delete a card")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCardByAdmin(cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cards")
    @Operation(summary = "Get all cards with raw card numbers")
    public ResponseEntity<Page<AdminCardDTO>> getAllCards(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cardService.getAllCardsRaw(page, size));
    }

    // Users
    @GetMapping("/users")
    @Operation(summary = "List users")
    public ResponseEntity<Page<AdminUserDTO>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsersForPage(pageable);
        Page<AdminUserDTO> dtos = users.map(user -> new AdminUserDTO(user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getCreatedAt()));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable Long userId) {
        User u = userService.findUserById(userId);
        return ResponseEntity.ok(new AdminUserDTO(u.getId(), u.getUsername(), u.getRole(), u.getCreatedAt()));
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
