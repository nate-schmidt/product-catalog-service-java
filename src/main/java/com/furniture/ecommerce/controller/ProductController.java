package com.furniture.ecommerce.controller;

import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;
import com.furniture.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * Constructor for ProductController.
     *
     * @param productService the product service
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Create a new product.
     *
     * @param productRequest the product data to create
     * @return the created product
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    /**
     * Get product by ID.
     *
     * @param id the product ID
     * @return the product
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable @Min(1) Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    /**
     * Get all products.
     *
     * @return list of all products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Update existing product.
     *
     * @param id the product ID
     * @param productRequest the updated product data
     * @return the updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Delete product.
     *
     * @param id the product ID
     * @return deletion confirmation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable @Min(1) Long id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search products with filters.
     *
     * @param category product category
     * @param material product material
     * @param color product color
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @param inStock whether product is in stock
     * @return list of matching products
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock) {
        List<ProductResponseDTO> products = productService.searchProducts(
                category, material, color, minPrice, maxPrice, inStock);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by category.
     *
     * @param category the product category
     * @return list of products in the category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Search products by name.
     *
     * @param query the search query
     * @return list of matching products
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(@RequestParam String query) {
        List<ProductResponseDTO> products = productService.searchByName(query);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by price range.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products in the price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<ProductResponseDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by dimensions.
     *
     * @param maxWidth maximum width
     * @param maxHeight maximum height
     * @param maxDepth maximum depth
     * @return list of products within dimensions
     */
    @GetMapping("/dimensions")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByDimensions(
            @RequestParam(required = false) Double maxWidth,
            @RequestParam(required = false) Double maxHeight,
            @RequestParam(required = false) Double maxDepth) {
        List<ProductResponseDTO> products = productService.getProductsByDimensions(maxWidth, maxHeight, maxDepth);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Update product stock.
     *
     * @param id the product ID
     * @param quantity the new stock quantity
     * @return the updated product
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable @Min(1) Long id,
            @RequestParam @Min(0) Integer quantity) {
        ProductResponseDTO updatedProduct = productService.updateStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Get in-stock products.
     *
     * @return list of products that are in stock
     */
    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponseDTO>> getInStockProducts() {
        List<ProductResponseDTO> products = productService.getInStockProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get low stock products.
     *
     * @param threshold the stock threshold
     * @return list of products with low stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts(
            @RequestParam(defaultValue = "10") @Min(1) Integer threshold) {
        List<ProductResponseDTO> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get available filters.
     *
     * @return map of available filter options
     */
    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getProductFilters() {
        Map<String, List<String>> filters = productService.getProductFilters();
        return ResponseEntity.ok(filters);
    }
    
    /**
     * Check if product name is unique.
     *
     * @param name the product name to check
     * @param excludeId optional product ID to exclude from check
     * @return whether the name is available
     */
    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkProductName(
            @RequestParam String name,
            @RequestParam(required = false) Long excludeId) {
        boolean isUnique = productService.isProductNameUnique(name, excludeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isUnique);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Batch create products.
     *
     * @param productRequests list of products to create
     * @return list of created products
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ProductResponseDTO>> createProducts(
            @Valid @RequestBody List<ProductRequestDTO> productRequests) {
        List<ProductResponseDTO> createdProducts = productService.createProducts(productRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProducts);
    }
    
    /**
     * Handle not found exceptions.
     *
     * @param e the exception
     * @return error response
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    /**
     * Handle bad request exceptions.
     *
     * @param e the exception
     * @return error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Handle generic exceptions.
     *
     * @param e the exception
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 
