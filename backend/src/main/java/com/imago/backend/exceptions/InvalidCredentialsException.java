package com.imago.backend.exceptions;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
