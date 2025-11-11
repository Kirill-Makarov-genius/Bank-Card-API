package com.example.bankcard_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankcard_api.DTO.CardDTO;
import com.example.bankcard_api.DTO.AdminCardDTO;
import com.example.bankcard_api.entity.Card;
import com.example.bankcard_api.DTO.TransferResponse;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.exception.CardAlreadyExistsException;
import com.example.bankcard_api.exception.CardNotFoundException;
import com.example.bankcard_api.exception.ForbiddenOperationException;
import com.example.bankcard_api.exception.InsufficientFundsException;
import com.example.bankcard_api.exception.SameCardTransferException;
import com.example.bankcard_api.exception.TransferOwnershipException;
import com.example.bankcard_api.repository.CardRepository;
import com.example.bankcard_api.enums.CardStatus;
import com.example.bankcard_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
    
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter EXPIRY_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final int EXPIRY_YEARS = 4; // default card validity period

    public List<CardDTO> getUserCards(User user){
        
        return cardRepository.findByUser(user)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public CardDTO createCard(User user, String cardNumber){
        
        cardRepository.findByCardNumber(cardNumber)
            .ifPresent((c) -> { throw new CardAlreadyExistsException(cardNumber); });
        
        YearMonth expiryYm = YearMonth.now().plusYears(EXPIRY_YEARS);
        String expiry = expiryYm.format(EXPIRY_FORMATTER);

        Card card = Card.builder()
            .user(user)
            .cardNumber(cardNumber)
            .expiry(expiry)
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

    @Transactional
    public TransferResponse transfer(User user, String fromCardNumber, String toCardNumber, BigDecimal amount) {
        if (fromCardNumber.equals(toCardNumber)) {
            throw new SameCardTransferException();
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Card from = cardRepository.findByCardNumber(fromCardNumber)
                .orElseThrow(() -> new CardNotFoundException(null));
        Card to = cardRepository.findByCardNumber(toCardNumber)
                .orElseThrow(() -> new CardNotFoundException(null));

        if (!from.getUser().getId().equals(user.getId())) {
            throw new TransferOwnershipException();
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepository.save(from);
        cardRepository.save(to);

        return new TransferResponse(toDTO(from), toDTO(to));
    }

    public AdminCardDTO toAdminDTO(Card card){
        return new AdminCardDTO(
            card.getId(),
            card.getUser().getId(),
            card.getUser().getUsername(),
            card.getCardNumber(),
            card.getBalance(),
            card.getCreatedAt(),
            card.getExpiry(),
            card.getStatus()
        );
    }

    // Admin operations
    public AdminCardDTO createCardForUser(Long userId, String cardNumber, String explicitExpiry) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new com.example.bankcard_api.exception.UserNotFoundException(userId));
        cardRepository.findByCardNumber(cardNumber)
            .ifPresent(c -> { throw new CardAlreadyExistsException(cardNumber); });

        String expiry = explicitExpiry;
        if (expiry == null || expiry.isBlank()) {
            YearMonth ym = YearMonth.now().plusYears(EXPIRY_YEARS);
            expiry = ym.format(EXPIRY_FORMATTER);
        }
        Card card = Card.builder()
            .user(user)
            .cardNumber(cardNumber)
            .expiry(expiry)
            .status(CardStatus.ACTIVE)
            .build();
        return toAdminDTO(cardRepository.save(card));
    }

    public AdminCardDTO setCardStatus(Long cardId, CardStatus status) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new CardNotFoundException(cardId));
        card.setStatus(status);
        return toAdminDTO(cardRepository.save(card));
    }

    public void deleteCardByAdmin(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new CardNotFoundException(cardId);
        }
        cardRepository.deleteById(cardId);
    }

    public List<AdminCardDTO> getAllCardsRaw(){
        return cardRepository.findAll().stream().map(this::toAdminDTO).collect(Collectors.toList());
    }

    private CardDTO toDTO(Card card){
        return new CardDTO(
            card.getId(),
            card.getCardNumber(),
            card.getBalance(),
            card.getCreatedAt(),
            card.getExpiry(),
            card.getStatus()
        );
    }


}
