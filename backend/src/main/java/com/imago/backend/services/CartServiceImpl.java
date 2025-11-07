package com.imago.backend.services;

import com.imago.backend.exceptions.ProductNotFoundException;
import com.imago.backend.exceptions.UserNotFoundException;
import com.imago.backend.models.Cart;
import com.imago.backend.models.CartItem;
import com.imago.backend.models.Product;
import com.imago.backend.models.User;
import com.imago.backend.repositories.CartItemRepository;
import com.imago.backend.repositories.CartRepository;
import com.imago.backend.repositories.ProductRepository;
import com.imago.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return getOrCreateCart(user);
    }

    @Override
    public CartItem addItemToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.getIsActive()) {
            throw new RuntimeException("Product is not available");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity);
            cart.addItem(newItem);
            return cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartItemRepository.findByCartId(cart.getId());
    }

    @Override
    public CartItem updateItemQuantity(Long userId, Long itemId, Integer quantity) {
        if (quantity < 1) {
            throw new RuntimeException("Quantity must be at least 1");
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        if (item.getProduct().getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + item.getProduct().getStock());
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public void removeItemFromCart(Long userId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateCartTotal(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getTotal();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCartEmpty(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems().isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalItemsCount(Long userId) {
        List<CartItem> items = getCartItems(userId);
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
