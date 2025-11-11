package com.example.bankcard_api.exception;

public class SameCardTransferException extends RuntimeException {
    public SameCardTransferException() {
        super("Cannot transfer to the same card");
    }
}
