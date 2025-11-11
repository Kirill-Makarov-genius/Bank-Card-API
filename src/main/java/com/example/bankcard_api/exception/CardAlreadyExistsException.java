package com.example.bankcard_api.exception;


public class CardAlreadyExistsException extends RuntimeException{

    public CardAlreadyExistsException(String cardNumber){
        super("Card with number " + cardNumber + " already exist");
    }

}
