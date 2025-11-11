package com.example.bankcard_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcard_api.entity.Card;
import com.example.bankcard_api.entity.User;

public interface CardRepository extends JpaRepository<Card, Long> {
    

    List<Card> findByUser(User user);
    
    Optional<Card> findByCardNumber(String cardNumber);

}
