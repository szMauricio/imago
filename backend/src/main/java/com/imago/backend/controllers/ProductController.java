package com.imago.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imago.backend.models.Product;
import com.imago.backend.models.dto.ProductDto;
import com.imago.backend.models.enums.Category;
import com.imago.backend.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductDto.Response> createProduct(@Valid @RequestBody ProductDto.CreateRequest request) {
        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory(),
                request.getImageUrl());
        Product createProduct = service.createProduct(product);
        ProductDto.Response response = new ProductDto.Response(createProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto.Response>> getAllProducts(
            @RequestParam(required = false) Boolean activeOnly) {
        List<Product> products;
        if (activeOnly != null && activeOnly) {
            products = service.getActiveProducts();
        } else {
            products = service.getAllProducts();
        }

        List<ProductDto.Response> response = products.stream()
                .map(ProductDto.Response::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.Response> getProductById(@PathVariable Long id) {
        Product product = service.getProductById(id);
        ProductDto.Response response = new ProductDto.Response(product);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto.Response>> getProductsByCategory(@PathVariable Category category) {
        List<ProductDto.Response> products = service.getProductsByCategory(category).stream()
                .map(ProductDto.Response::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto.Response>> searchProducts(@RequestParam String name) {
        List<ProductDto.Response> products = service.searchProductsByName(name).stream()
                .map(ProductDto.Response::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto.Response> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto.UpdateRequest request) {

        Product productDetails = new Product();
        productDetails.setName(request.getName());
        productDetails.setDescription(request.getDescription());
        productDetails.setPrice(request.getPrice());
        productDetails.setStock(request.getStock());
        productDetails.setCategory(request.getCategory());
        productDetails.setImageUrl(request.getImageUrl());
        productDetails.setIsActive(request.getIsActive());

        Product updatedProduct = service.updateProduct(id, productDetails);
        ProductDto.Response response = new ProductDto.Response(updatedProduct);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDto.Response> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {

        Product updatedProduct = service.updateStock(id, stock);
        ProductDto.Response response = new ProductDto.Response(updatedProduct);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        service.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
