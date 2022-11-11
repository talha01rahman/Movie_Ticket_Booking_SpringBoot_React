package com.team4.backend.exception;

public class InvalidTicketInfoException extends RuntimeException {
    public InvalidTicketInfoException(String message) {
        super(message);
    }
}
