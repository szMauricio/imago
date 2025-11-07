package com.imago.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imago.backend.models.Cart;
import com.imago.backend.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Query("SELECT COUNT(ci) FROM Cart c JOIN c.items ci WHERE c.user.id = :userId")
    Long countItemsByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(ci.product.price * ci.quantity) FROM Cart c JOIN c.items ci WHERE c.user.id = :userId")
    Double calculateTotalByUserId(@Param("userId") Long userId);
}
