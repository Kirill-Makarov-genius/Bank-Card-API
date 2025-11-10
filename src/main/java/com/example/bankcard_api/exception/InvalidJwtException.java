package com.example.bankcard_api.exception;


public class InvalidJwtException extends RuntimeException {
    
    public InvalidJwtException(String message, Throwable cause){
        super(message, cause);
    }
}
