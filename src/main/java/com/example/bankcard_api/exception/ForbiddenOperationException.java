package com.example.bankcard_api.exception;

public class ForbiddenOperationException extends RuntimeException {
    
    public ForbiddenOperationException(String message){
        super(message);
    }

}
