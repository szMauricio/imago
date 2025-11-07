package com.imago.backend.services;

import java.util.List;

import com.imago.backend.models.Product;
import com.imago.backend.models.enums.Category;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getActiveProducts();

    Product getProductById(Long id);

    List<Product> getProductsByCategory(Category category);

    List<Product> searchProductsByName(String name);

    List<Product> getAvailableProducts();

    Product updateProduct(Long id, Product productDetails);

    Product updateStock(Long id, Integer newStock);

    void deactivateProduct(Long id);

    void deleteProduct(Long id);
}
