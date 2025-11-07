package com.imago.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.imago.backend.exceptions.InvalidStockException;
import com.imago.backend.exceptions.ProductNotFoundException;
import com.imago.backend.models.Product;
import com.imago.backend.models.enums.Category;
import com.imago.backend.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getStock() < 0) {
            throw new InvalidStockException();
        }

        return repository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public List<Product> getActiveProducts() {
        return repository.findByIsActiveTrue();
    }

    @Override
    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return repository.findByCategoryAndIsActiveTrue(category);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> getAvailableProducts() {
        return repository.findByIsActiveTrueAndStockGreaterThan(0);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getStock() != null) {
            if (productDetails.getStock() < 0) {
                throw new InvalidStockException();
            }
            product.setStock(productDetails.getStock());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }
        if (productDetails.getIsActive() != null) {
            product.setIsActive(productDetails.getIsActive());
        }

        return repository.save(product);
    }

    @Override
    public Product updateStock(Long id, Integer newStock) {
        if (newStock < 0) {
            throw new InvalidStockException();
        }

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setStock(newStock);
        return repository.save(product);
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setIsActive(false);
        repository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        repository.delete(product);
    }
}
