package com.team4.backend.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
