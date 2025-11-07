package com.imago.backend.models.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartDto {
    public static class AddItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class UpdateItemRequest {
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Double productPrice;
        private Integer quantity;
        private Double subtotal;

        public CartItemResponse(com.imago.backend.models.CartItem cartItem) {
            this.id = cartItem.getId();
            this.productId = cartItem.getProduct().getId();
            this.productName = cartItem.getProduct().getName();
            this.productPrice = cartItem.getProduct().getPrice().doubleValue();
            this.quantity = cartItem.getQuantity();
            this.subtotal = cartItem.getSubtotal();
        }

        public Long getId() {
            return id;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public Double getProductPrice() {
            return productPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Double getSubtotal() {
            return subtotal;
        }
    }

    public static class CartResponse {
        private Long id;
        private Long userId;
        private List<CartItemResponse> items;
        private Double total;
        private Integer totalItems;

        public CartResponse(com.imago.backend.models.Cart cart) {
            this.id = cart.getId();
            this.userId = cart.getUser().getId();
            this.items = cart.getItems().stream()
                    .map(CartItemResponse::new)
                    .collect(java.util.stream.Collectors.toList());
            this.total = cart.getTotal();
            this.totalItems = cart.getItems().stream()
                    .mapToInt(com.imago.backend.models.CartItem::getQuantity)
                    .sum();
        }

        public Long getId() {
            return id;
        }

        public Long getUserId() {
            return userId;
        }

        public List<CartItemResponse> getItems() {
            return items;
        }

        public Double getTotal() {
            return total;
        }

        public Integer getTotalItems() {
            return totalItems;
        }
    }
}
