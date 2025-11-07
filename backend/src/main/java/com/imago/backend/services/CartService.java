package com.imago.backend.services;

import java.util.List;

import com.imago.backend.models.Cart;
import com.imago.backend.models.CartItem;
import com.imago.backend.models.User;

public interface CartService {
    Cart getOrCreateCart(User user);

    Cart getOrCreateCart(Long userId);

    CartItem addItemToCart(Long userId, Long productId, Integer quantity);

    List<CartItem> getCartItems(Long userId);

    CartItem updateItemQuantity(Long userId, Long itemId, Integer quantity);

    void removeItemFromCart(Long userId, Long itemId);

    void clearCart(Long userId);

    Double calculateCartTotal(Long userId);

    boolean isCartEmpty(Long userId);

    Integer getTotalItemsCount(Long userId);
}
