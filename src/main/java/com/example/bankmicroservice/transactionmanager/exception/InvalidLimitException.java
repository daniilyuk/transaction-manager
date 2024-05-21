package com.example.bankmicroservice.transactionmanager.exception;

public class InvalidLimitException extends RuntimeException{
    public InvalidLimitException(String message) {
        super(message);
    }
}
