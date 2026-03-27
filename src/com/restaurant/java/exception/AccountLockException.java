package com.restaurant.java.exception;

public class AccountLockException extends Exception{
    public AccountLockException(String message) {
        super(message);
    }
}
