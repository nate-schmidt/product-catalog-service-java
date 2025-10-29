package com.furniture.ecommerce.service;

import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;
import com.furniture.ecommerce.model.Product;
import com.furniture.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    private ProductRequestDTO validProductRequest;
    private Product sampleProduct;
    
    @BeforeEach
    void setUp() {
        validProductRequest = new ProductRequestDTO();
        validProductRequest.setName("Test Sofa");
        validProductRequest.setCategory("Sofas");
        validProductRequest.setPrice(new BigDecimal("999.99"));
        validProductRequest.setStock(10);
        validProductRequest.setDescription("A test sofa");
        
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Test Sofa");
        sampleProduct.setCategory("Sofas");
        sampleProduct.setPrice(new BigDecimal("999.99"));
        sampleProduct.setStock(10);
        sampleProduct.setDescription("A test sofa");
    }
    
    @Test
    void createProduct_WithValidData_ShouldReturnProductResponse() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        
        // When
        ProductResponseDTO response = productService.createProduct(validProductRequest);
        
        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Sofa", response.getName());
        assertEquals(new BigDecimal("999.99"), response.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    
    @Test
    void getProductById_WithExistingId_ShouldReturnProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        
        // When
        ProductResponseDTO response = productService.getProductById(1L);
        
        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Sofa", response.getName());
        verify(productRepository, times(1)).findById(1L);
    }
    
    @Test
    void getProductById_WithNonExistingId_ShouldThrowException() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            productService.getProductById(999L);
        });
        verify(productRepository, times(1)).findById(999L);
    }
    
    @Test
    void updateStock_WithValidQuantity_ShouldUpdateStock() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        
        // When
        ProductResponseDTO response = productService.updateStock(1L, 25);
        
        // Then
        assertNotNull(response);
        assertEquals(25, sampleProduct.getStock());
        verify(productRepository, times(1)).save(sampleProduct);
    }
    
    @Test
    void updateStock_WithNegativeQuantity_ShouldThrowException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateStock(1L, -5);
        });
        verify(productRepository, never()).save(any());
    }
    
    @Test
    void deleteProduct_WithExistingId_ShouldDelete() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        
        // When
        productService.deleteProduct(1L);
        
        // Then
        verify(productRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deleteProduct_WithNonExistingId_ShouldThrowException() {
        // Given
        when(productRepository.existsById(999L)).thenReturn(false);
        
        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            productService.deleteProduct(999L);
        });
        verify(productRepository, never()).deleteById(any());
    }
} 
