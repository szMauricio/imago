package com.imago.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.imago.backend.models.Order;
import com.imago.backend.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi.product.id, SUM(oi.quantity) as totalSold " +
            "FROM OrderItem oi " +
            "GROUP BY oi.product.id " +
            "ORDER BY totalSold DESC")
    List<Object[]> findBestSellingProducts();

    @Query("SELECT oi.product.id, SUM(oi.unitPrice * oi.quantity) as totalRevenue " +
            "FROM OrderItem oi " +
            "GROUP BY oi.product.id " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> findProductRevenue();
}
