package com.imago.backend.services;

import java.util.List;

import com.imago.backend.models.Order;
import com.imago.backend.models.enums.OrderStatus;

public interface OrderService {
    Order createOrderFromCart(Long userId, String shippingAddress);

    Order getOrderById(Long orderId);

    Order getOrderByNumber(String orderNumber);

    List<Order> getUserOrders(Long userId);

    List<Order> getAllOrders();

    Order updateOrderStatus(Long orderId, OrderStatus status);

    Order cancelOrder(Long orderId, Long userId);

    List<Order> getRecentOrders();

    Double getTotalSalesByStatus(OrderStatus status);
}
