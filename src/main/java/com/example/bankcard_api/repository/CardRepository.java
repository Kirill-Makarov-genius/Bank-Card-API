package com.example.bankcard_api.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcard_api.entity.Card;
import com.example.bankcard_api.entity.User;

public interface CardRepository extends JpaRepository<Card, Long> {
    

    Page<Card> findAllByUser(User user, Pageable pageable);
    

    Optional<Card> findByCardNumber(String cardNumber);

}
