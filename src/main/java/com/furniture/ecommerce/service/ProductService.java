package com.furniture.ecommerce.service;

import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {
    
    /**
     * Create a new product.
     *
     * @param productRequest the product data to create
     * @return the created product
     */
    ProductResponseDTO createProduct(ProductRequestDTO productRequest);
    
    /**
     * Get product by ID.
     *
     * @param id the product ID
     * @return the product
     */
    ProductResponseDTO getProductById(Long id);
    
    /**
     * Get all products.
     *
     * @return list of all products
     */
    List<ProductResponseDTO> getAllProducts();
    
    /**
     * Update existing product.
     *
     * @param id the product ID
     * @param productRequest the updated product data
     * @return the updated product
     */
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest);
    
    /**
     * Delete product.
     *
     * @param id the product ID
     */
    void deleteProduct(Long id);
    
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
    List<ProductResponseDTO> searchProducts(String category, String material, String color, 
                                          BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);
    
    /**
     * Get products by category.
     *
     * @param category the product category
     * @return list of products in the category
     */
    List<ProductResponseDTO> getProductsByCategory(String category);
    
    /**
     * Get products by price range.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products in the price range
     */
    List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Search products by name.
     *
     * @param name the search query
     * @return list of matching products
     */
    List<ProductResponseDTO> searchByName(String name);
    
    /**
     * Get products by dimensions.
     *
     * @param maxWidth maximum width
     * @param maxHeight maximum height
     * @param maxDepth maximum depth
     * @return list of products within dimensions
     */
    List<ProductResponseDTO> getProductsByDimensions(Double maxWidth, Double maxHeight, Double maxDepth);
    
    /**
     * Update product stock.
     *
     * @param id the product ID
     * @param quantity the new stock quantity
     * @return the updated product
     */
    ProductResponseDTO updateStock(Long id, Integer quantity);
    
    /**
     * Get in-stock products.
     *
     * @return list of products that are in stock
     */
    List<ProductResponseDTO> getInStockProducts();
    
    /**
     * Get low stock products.
     *
     * @param threshold the stock threshold
     * @return list of products with low stock
     */
    List<ProductResponseDTO> getLowStockProducts(Integer threshold);
    
    /**
     * Get available filters.
     *
     * @return map of available filter options
     */
    Map<String, List<String>> getProductFilters();
    
    /**
     * Check if product name is unique.
     *
     * @param name the product name to check
     * @param excludeId optional product ID to exclude from check
     * @return whether the name is available
     */
    boolean isProductNameUnique(String name, Long excludeId);
    
    /**
     * Batch create products.
     *
     * @param productRequests list of products to create
     * @return list of created products
     */
    List<ProductResponseDTO> createProducts(List<ProductRequestDTO> productRequests);
} 
