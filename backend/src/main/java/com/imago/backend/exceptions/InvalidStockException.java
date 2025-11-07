package com.imago.backend.exceptions;

public class InvalidStockException extends BusinessException {
    public InvalidStockException() {
        super("Stock cannot be negative");
    }

    public InvalidStockException(String message) {
        super(message);
    }
}
