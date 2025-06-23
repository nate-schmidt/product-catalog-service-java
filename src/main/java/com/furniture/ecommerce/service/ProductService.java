package com.furniture.ecommerce.service;

import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;
import com.furniture.ecommerce.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {
    
    // Basic CRUD operations
    ProductResponseDTO createProduct(ProductRequestDTO productRequest);
    
    ProductResponseDTO getProductById(Long id);
    
    List<ProductResponseDTO> getAllProducts();
    
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest);
    
    void deleteProduct(Long id);
    
    // Search and filter operations
    List<ProductResponseDTO> searchProducts(String category, String material, String color, 
                                          BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);
    
    List<ProductResponseDTO> getProductsByCategory(String category);
    
    List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<ProductResponseDTO> searchByName(String name);
    
    List<ProductResponseDTO> getProductsByDimensions(Double maxWidth, Double maxHeight, Double maxDepth);
    
    // Inventory operations
    ProductResponseDTO updateStock(Long id, Integer quantity);
    
    List<ProductResponseDTO> getInStockProducts();
    
    List<ProductResponseDTO> getLowStockProducts(Integer threshold);
    
    // Utility operations
    Map<String, List<String>> getProductFilters();
    
    boolean isProductNameUnique(String name, Long excludeId);
    
    // Batch operations
    List<ProductResponseDTO> createProducts(List<ProductRequestDTO> productRequests);
} 