package com.furniture.ecommerce.controller;

import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;
import com.furniture.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    // Create a new product
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable @Min(1) Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    // Update existing product
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }
    
    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable @Min(1) Long id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }
    
    // Search products with filters
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
    
    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    // Search products by name
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(@RequestParam String query) {
        List<ProductResponseDTO> products = productService.searchByName(query);
        return ResponseEntity.ok(products);
    }
    
    // Get products by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<ProductResponseDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    // Get products by dimensions
    @GetMapping("/dimensions")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByDimensions(
            @RequestParam(required = false) Double maxWidth,
            @RequestParam(required = false) Double maxHeight,
            @RequestParam(required = false) Double maxDepth) {
        List<ProductResponseDTO> products = productService.getProductsByDimensions(maxWidth, maxHeight, maxDepth);
        return ResponseEntity.ok(products);
    }
    
    // Update product stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable @Min(1) Long id,
            @RequestParam @Min(0) Integer quantity) {
        ProductResponseDTO updatedProduct = productService.updateStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    // Get in-stock products
    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponseDTO>> getInStockProducts() {
        List<ProductResponseDTO> products = productService.getInStockProducts();
        return ResponseEntity.ok(products);
    }
    
    // Get low stock products
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts(
            @RequestParam(defaultValue = "10") @Min(1) Integer threshold) {
        List<ProductResponseDTO> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
    
    // Get available filters
    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getProductFilters() {
        Map<String, List<String>> filters = productService.getProductFilters();
        return ResponseEntity.ok(filters);
    }
    
    // Check if product name is unique
    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkProductName(
            @RequestParam String name,
            @RequestParam(required = false) Long excludeId) {
        boolean isUnique = productService.isProductNameUnique(name, excludeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isUnique);
        return ResponseEntity.ok(response);
    }
    
    // Batch create products
    @PostMapping("/batch")
    public ResponseEntity<List<ProductResponseDTO>> createProducts(
            @Valid @RequestBody List<ProductRequestDTO> productRequests) {
        List<ProductResponseDTO> createdProducts = productService.createProducts(productRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProducts);
    }
    
    // Exception handlers
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 