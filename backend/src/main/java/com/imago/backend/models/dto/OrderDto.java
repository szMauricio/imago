package com.imago.backend.models.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.imago.backend.models.enums.OrderStatus;

import jakarta.validation.constraints.NotBlank;

public class OrderDto {
    public static class CreateRequest {
        @NotBlank(message = "Shipping address is required")
        private String shippingAddress;

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }
    }

    public static class UpdateStatusRequest {
        private OrderStatus status;

        public OrderStatus getStatus() {
            return status;
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }
    }

    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Double unitPrice;
        private Integer quantity;
        private Double subtotal;

        public OrderItemResponse(com.imago.backend.models.OrderItem orderItem) {
            this.id = orderItem.getId();
            this.productId = orderItem.getProduct().getId();
            this.productName = orderItem.getProduct().getName();
            this.unitPrice = orderItem.getUnitPrice().doubleValue();
            this.quantity = orderItem.getQuantity();
            this.subtotal = orderItem.getSubtotal().doubleValue();
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

        public Double getUnitPrice() {
            return unitPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Double getSubtotal() {
            return subtotal;
        }
    }

    public static class OrderResponse {
        private Long id;
        private String orderNumber;
        private Long userId;
        private String userEmail;
        private OrderStatus status;
        private List<OrderItemResponse> items;
        private Double totalAmount;
        private String shippingAddress;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public OrderResponse(com.imago.backend.models.Order order) {
            this.id = order.getId();
            this.orderNumber = order.getOrderNumber();
            this.userId = order.getUser().getId();
            this.userEmail = order.getUser().getEmail();
            this.status = order.getStatus();
            this.items = order.getItems().stream()
                    .map(OrderItemResponse::new)
                    .collect(java.util.stream.Collectors.toList());
            this.totalAmount = order.getTotalAmount().doubleValue();
            this.shippingAddress = order.getShippingAddress();
            this.createdAt = order.getCreatedAt();
            this.updatedAt = order.getUpdatedAt();
        }

        public Long getId() {
            return id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public List<OrderItemResponse> getItems() {
            return items;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
