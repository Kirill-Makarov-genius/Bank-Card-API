package com.example.bankcard_api.exception;

public class TransferOwnershipException extends RuntimeException {
    public TransferOwnershipException() {
        super("You cannot transfer from another user's card");
    }
}
