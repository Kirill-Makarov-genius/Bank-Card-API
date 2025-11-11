package com.example.bankcard_api.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient balance");
    }
    public InsufficientFundsException(String message) {
        super(message);
    }
}
