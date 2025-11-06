package com.imago.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imago.backend.models.Product;
import com.imago.backend.models.enums.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByIsActiveTrue();

    List<Product> findByCategoryAndIsActiveTrue(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByIsActiveTrueAndStockGreaterThan(Integer stock);
}
