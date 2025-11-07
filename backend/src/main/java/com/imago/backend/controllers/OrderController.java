package com.imago.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imago.backend.models.dto.OrderDto;
import com.imago.backend.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto.OrderResponse> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody OrderDto.CreateRequest request) {

        var order = orderService.createOrderFromCart(userId, request.getShippingAddress());
        var response = new OrderDto.OrderResponse(order);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto.OrderResponse> getOrderById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId) {

        var order = orderService.getOrderById(orderId);

        if (!order.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var response = new OrderDto.OrderResponse(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto.OrderResponse>> getUserOrders(@RequestHeader("X-User-Id") Long userId) {
        var orders = orderService.getUserOrders(userId);
        var response = orders.stream()
                .map(OrderDto.OrderResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto.OrderResponse> cancelOrder(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId) {

        var order = orderService.cancelOrder(orderId, userId);
        var response = new OrderDto.OrderResponse(order);

        return ResponseEntity.ok(response);
    }

    // Endpoints para administradores
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderDto.OrderResponse>> getAllOrders() {
        var orders = orderService.getAllOrders();
        var response = orders.stream()
                .map(OrderDto.OrderResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<OrderDto.OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderDto.UpdateStatusRequest request) {

        var order = orderService.updateOrderStatus(orderId, request.getStatus());
        var response = new OrderDto.OrderResponse(order);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/recent")
    public ResponseEntity<List<OrderDto.OrderResponse>> getRecentOrders() {
        var orders = orderService.getRecentOrders();
        var response = orders.stream()
                .map(OrderDto.OrderResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }
}
