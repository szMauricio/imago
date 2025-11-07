package com.imago.backend.exceptions;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
