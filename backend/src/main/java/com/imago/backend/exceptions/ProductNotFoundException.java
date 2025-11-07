package com.imago.backend.exceptions;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
