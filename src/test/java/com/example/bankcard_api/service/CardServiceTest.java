package com.example.bankcard_api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.bankcard_api.DTO.CardDTO;
import com.example.bankcard_api.entity.Card;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.repository.CardRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).username("user1").build();
    }

    @Test
    void testCreateCard() {

        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardDTO created = cardService.createCard(user, "1234567812345678");
        assertNotNull(created);
        assertEquals("1234567812345678", created.getCardNumber());
    }

    @Test
    void testUpdateBalance() {
        Card card = Card.builder().id(1L).balance(BigDecimal.ZERO).build();
        User user = User.builder().id(1L).cards(List.of(card)).build();
        card.setUser(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDTO updated = cardService.updateBalance(1L, new BigDecimal("100.50"), user, false);
        assertEquals(new BigDecimal("100.50"), updated.getBalance());
    }

    @Test
    void testUpdateBalanceThrowsIfCardNotFound() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cardService.updateBalance(2L, BigDecimal.TEN, new User(), false));
    }
}
