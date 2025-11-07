package com.imago.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imago.backend.models.Order;
import com.imago.backend.models.User;
import com.imago.backend.models.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= FUNCTION('DATE_SUB', CURRENT_DATE, 30, 'DAY') ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersLast30Days();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = :status")
    Double calculateTotalSalesByStatus(@Param("status") OrderStatus status);

    Long countByStatus(OrderStatus status);

    List<Order> findByUserAndStatus(User user, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
