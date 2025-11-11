package com.example.bankcard_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.bankcard_api.DTO.CardDTO;
import com.example.bankcard_api.entity.Card;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.exception.CardAlreadyExistsException;
import com.example.bankcard_api.exception.CardNotFoundException;
import com.example.bankcard_api.exception.ForbiddenOperationException;
import com.example.bankcard_api.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
    
    private final CardRepository cardRepository;

    public List<CardDTO> getUserCards(User user){
        
        return cardRepository.findByUser(user)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public CardDTO createCard(User user, String cardNumber){
        
        cardRepository.findByCardNumber(cardNumber)
            .ifPresent((c) -> { throw new CardAlreadyExistsException(cardNumber); });
        
        Card card = Card.builder()
            .user(user)
            .cardNumber(cardNumber)
            .build();
        
        return toDTO(cardRepository.save(card));
    }

    public CardDTO updateBalance(Long cardId, BigDecimal newBalance, User user, boolean isAdmin){
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new CardNotFoundException(cardId));

        if (!isAdmin && !card.getUser().getId().equals(user.getId())){
            throw new ForbiddenOperationException("You cannot update another user's card");
        }

        card.setBalance(newBalance);
        
        return toDTO(cardRepository.save(card));
    }

    public void deleteCard(Long cardId, User user, boolean isAdmin){
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new CardNotFoundException(cardId));


        if (!isAdmin && !card.getUser().getId().equals(user.getId())){
            throw new ForbiddenOperationException("You cannot update another user's card");
        }

        cardRepository.deleteById(cardId);
    }

    private CardDTO toDTO(Card card){
        return new CardDTO(
            card.getId(),
            card.getCardNumber(),
            card.getBalance(),
            card.getCreatedAt()
        );
    }


}
