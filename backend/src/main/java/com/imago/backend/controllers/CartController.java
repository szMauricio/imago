package com.imago.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imago.backend.models.dto.CartDto;
import com.imago.backend.services.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto.CartItemResponse> addItemToCart(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CartDto.AddItemRequest request) {

        var cartItem = service.addItemToCart(userId, request.getProductId(), request.getQuantity());
        var response = new CartDto.CartItemResponse(cartItem);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartDto.CartResponse> getCart(@RequestHeader("X-User-Id") Long userId) {
        var cart = service.getOrCreateCart(userId);
        var response = new CartDto.CartResponse(cart);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartDto.CartItemResponse>> getCartItems(@RequestHeader("X-User-Id") Long userId) {
        var cartItems = service.getCartItems(userId);
        var response = cartItems.stream()
                .map(CartDto.CartItemResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto.CartItemResponse> updateItemQuantity(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CartDto.UpdateItemRequest request) {

        var cartItem = service.updateItemQuantity(userId, itemId, request.getQuantity());
        var response = new CartDto.CartItemResponse(cartItem);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long itemId) {

        service.removeItemFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-Id") Long userId) {
        service.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(@RequestHeader("X-User-Id") Long userId) {
        var total = service.calculateCartTotal(userId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalItemsCount(@RequestHeader("X-User-Id") Long userId) {
        var count = service.getTotalItemsCount(userId);
        return ResponseEntity.ok(count);
    }
}
