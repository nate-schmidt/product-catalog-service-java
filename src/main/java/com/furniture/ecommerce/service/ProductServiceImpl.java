package com.furniture.ecommerce.service;

import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;
import com.furniture.ecommerce.model.Product;
import com.furniture.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    /**
     * Constructor for ProductServiceImpl.
     *
     * @param productRepository the product repository
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        validateProductRequest(productRequest);
        
        Product product = new Product();
        mapRequestToProduct(productRequest, product);
        
        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromProduct(savedProduct);
    }
    
    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
        return ProductResponseDTO.fromProduct(product);
    }
    
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
        
        validateProductRequest(productRequest);
        mapRequestToProduct(productRequest, existingProduct);
        
        Product updatedProduct = productRepository.save(existingProduct);
        return ProductResponseDTO.fromProduct(updatedProduct);
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    @Override
    public List<ProductResponseDTO> searchProducts(String category, String material, String color,
                                                 BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        return productRepository.searchProducts(category, material, color, minPrice, maxPrice, inStock)
                .stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category).stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        return productRepository.findByPriceBetween(
                minPrice != null ? minPrice : BigDecimal.ZERO,
                maxPrice != null ? maxPrice : new BigDecimal("999999.99")
        ).stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponseDTO> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponseDTO> getProductsByDimensions(Double maxWidth, Double maxHeight, Double maxDepth) {
        return productRepository.findByMaxDimensions(maxWidth, maxHeight, maxDepth).stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductResponseDTO updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
        
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        product.setStock(quantity);
        Product updatedProduct = productRepository.save(product);
        return ProductResponseDTO.fromProduct(updatedProduct);
    }
    
    @Override
    public List<ProductResponseDTO> getInStockProducts() {
        return productRepository.findByStockGreaterThan(0).stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponseDTO> getLowStockProducts(Integer threshold) {
        final int effectiveThreshold = (threshold == null || threshold < 0) ? 10 : threshold;
        return productRepository.findByStockGreaterThanEqual(1).stream()
                .filter(product -> product.getStock() <= effectiveThreshold)
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, List<String>> getProductFilters() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("categories", productRepository.findDistinctCategories());
        filters.put("materials", productRepository.findDistinctMaterials());
        filters.put("colors", productRepository.findDistinctColors());
        return filters;
    }
    
    @Override
    public boolean isProductNameUnique(String name, Long excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name.trim());
        if (excludeId != null) {
            products = products.stream()
                    .filter(p -> !p.getId().equals(excludeId))
                    .collect(Collectors.toList());
        }
        return products.isEmpty();
    }
    
    @Override
    @Transactional
    public List<ProductResponseDTO> createProducts(List<ProductRequestDTO> productRequests) {
        List<Product> products = new ArrayList<>();
        
        for (ProductRequestDTO request : productRequests) {
            validateProductRequest(request);
            Product product = new Product();
            mapRequestToProduct(request, product);
            products.add(product);
        }
        
        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts.stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }
    
    // Helper methods
    private void validateProductRequest(ProductRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (request.getStock() == null || request.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required");
        }
    }
    
    private void mapRequestToProduct(ProductRequestDTO request, Product product) {
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory().trim());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setWidth(request.getWidth());
        product.setHeight(request.getHeight());
        product.setDepth(request.getDepth());
        product.setMaterial(request.getMaterial() != null ? request.getMaterial().trim() : null);
        product.setColor(request.getColor() != null ? request.getColor().trim() : null);
        product.setImageUrl(request.getImageUrl());
    }
} 
