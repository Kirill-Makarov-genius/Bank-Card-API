package com.example.bankcard_api.exception;

public class CardNotFoundException extends RuntimeException {
    
    public CardNotFoundException(Long id){
        super("Card not found by this id: " + id);
    }

}
