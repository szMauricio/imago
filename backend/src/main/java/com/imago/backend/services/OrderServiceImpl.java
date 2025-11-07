package com.imago.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imago.backend.exceptions.OrderNotFoundException;
import com.imago.backend.exceptions.UserNotFoundException;
import com.imago.backend.models.CartItem;
import com.imago.backend.models.Order;
import com.imago.backend.models.OrderItem;
import com.imago.backend.models.Product;
import com.imago.backend.models.User;
import com.imago.backend.models.enums.OrderStatus;
import com.imago.backend.repositories.OrderRepository;
import com.imago.backend.repositories.ProductRepository;
import com.imago.backend.repositories.UserRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
            UserRepository userRepository,
            CartService cartService,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrderFromCart(Long userId, String shippingAddress) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<CartItem> cartItems = cartService.getCartItems(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getStock());
            }
        }

        Order order = new Order(user, null, shippingAddress);
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem(order, product, cartItem.getQuantity());
            order.addItem(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order = orderRepository.save(order);

        cartService.clearCart(userId);

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with number: " + orderNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED && status != OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot change status of delivered order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot change status of cancelled order");
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Order does not belong to user");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getRecentOrders() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        return orderRepository.findRecentOrders(startDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalSalesByStatus(OrderStatus status) {
        Double total = orderRepository.calculateTotalSalesByStatus(status);
        return total != null ? total : 0.0;
    }
}