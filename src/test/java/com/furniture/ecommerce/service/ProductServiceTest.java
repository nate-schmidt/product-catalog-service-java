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
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    
    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        List<Product> products = Arrays.asList(sampleProduct, createSecondProduct());
        when(productRepository.findAll()).thenReturn(products);
        
        // When
        List<ProductResponseDTO> response = productService.getAllProducts();
        
        // Then
        assertEquals(2, response.size());
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    void updateProduct_WithValidData_ShouldUpdateProduct() {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Name");
        existingProduct.setPrice(new BigDecimal("500.00"));
        existingProduct.setStock(5);
        existingProduct.setCategory("Old Category");
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        
        // When
        ProductResponseDTO response = productService.updateProduct(1L, validProductRequest);
        
        // Then
        assertNotNull(response);
        assertEquals("Test Sofa", existingProduct.getName());
        assertEquals(new BigDecimal("999.99"), existingProduct.getPrice());
        verify(productRepository, times(1)).save(existingProduct);
    }
    
    @Test
    void updateProduct_WithNonExistingId_ShouldThrowException() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            productService.updateProduct(999L, validProductRequest);
        });
        verify(productRepository, never()).save(any());
    }
    
    @Test
    void searchProducts_WithFilters_ShouldReturnFilteredProducts() {
        // Given
        List<Product> filteredProducts = Arrays.asList(sampleProduct);
        when(productRepository.searchProducts("Sofas", "Fabric", "Blue", 
                new BigDecimal("500"), new BigDecimal("1500"), true))
                .thenReturn(filteredProducts);
        
        // When
        List<ProductResponseDTO> response = productService.searchProducts(
                "Sofas", "Fabric", "Blue", new BigDecimal("500"), new BigDecimal("1500"), true);
        
        // Then
        assertEquals(1, response.size());
        assertEquals("Test Sofa", response.get(0).getName());
    }
    
    @Test
    void getProductsByCategory_ShouldReturnCategoryProducts() {
        // Given
        List<Product> sofas = Arrays.asList(sampleProduct);
        when(productRepository.findByCategoryIgnoreCase("Sofas")).thenReturn(sofas);
        
        // When
        List<ProductResponseDTO> response = productService.getProductsByCategory("Sofas");
        
        // Then
        assertEquals(1, response.size());
        assertEquals("Test Sofa", response.get(0).getName());
    }
    
    @Test
    void getProductsByPriceRange_WithValidRange_ShouldReturnProducts() {
        // Given
        List<Product> products = Arrays.asList(sampleProduct);
        BigDecimal min = new BigDecimal("500");
        BigDecimal max = new BigDecimal("1500");
        when(productRepository.findByPriceBetween(min, max)).thenReturn(products);
        
        // When
        List<ProductResponseDTO> response = productService.getProductsByPriceRange(min, max);
        
        // Then
        assertEquals(1, response.size());
        verify(productRepository, times(1)).findByPriceBetween(min, max);
    }
    
    @Test
    void getProductsByPriceRange_WithMinGreaterThanMax_ShouldThrowException() {
        // Given
        BigDecimal min = new BigDecimal("1500");
        BigDecimal max = new BigDecimal("500");
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByPriceRange(min, max);
        });
        verify(productRepository, never()).findByPriceBetween(any(), any());
    }
    
    @Test
    void getProductsByPriceRange_WithNullValues_ShouldUseDefaults() {
        // Given
        List<Product> products = Arrays.asList(sampleProduct);
        when(productRepository.findByPriceBetween(BigDecimal.ZERO, new BigDecimal("999999.99")))
                .thenReturn(products);
        
        // When
        List<ProductResponseDTO> response = productService.getProductsByPriceRange(null, null);
        
        // Then
        assertEquals(1, response.size());
        verify(productRepository, times(1)).findByPriceBetween(BigDecimal.ZERO, new BigDecimal("999999.99"));
    }
    
    @Test
    void searchByName_ShouldReturnMatchingProducts() {
        // Given
        List<Product> products = Arrays.asList(sampleProduct);
        when(productRepository.findByNameContainingIgnoreCase("sofa")).thenReturn(products);
        
        // When
        List<ProductResponseDTO> response = productService.searchByName("sofa");
        
        // Then
        assertEquals(1, response.size());
        assertEquals("Test Sofa", response.get(0).getName());
    }
    
    @Test
    void getProductsByDimensions_ShouldReturnMatchingProducts() {
        // Given
        List<Product> products = Arrays.asList(sampleProduct);
        when(productRepository.findByMaxDimensions(200.0, 100.0, 80.0)).thenReturn(products);
        
        // When
        List<ProductResponseDTO> response = productService.getProductsByDimensions(200.0, 100.0, 80.0);
        
        // Then
        assertEquals(1, response.size());
        verify(productRepository, times(1)).findByMaxDimensions(200.0, 100.0, 80.0);
    }
    
    @Test
    void getInStockProducts_ShouldReturnInStockProducts() {
        // Given
        List<Product> inStockProducts = Arrays.asList(sampleProduct);
        when(productRepository.findByStockGreaterThan(0)).thenReturn(inStockProducts);
        
        // When
        List<ProductResponseDTO> response = productService.getInStockProducts();
        
        // Then
        assertEquals(1, response.size());
        assertTrue(response.get(0).isInStock());
    }
    
    @Test
    void getLowStockProducts_WithThreshold_ShouldReturnLowStockProducts() {
        // Given
        Product lowStockProduct = createLowStockProduct();
        List<Product> lowStockProducts = Arrays.asList(lowStockProduct);
        when(productRepository.findByStockGreaterThanEqual(1)).thenReturn(lowStockProducts);
        
        // When
        List<ProductResponseDTO> response = productService.getLowStockProducts(5);
        
        // Then
        assertEquals(1, response.size());
        assertTrue(response.get(0).getStock() <= 5);
    }
    
    @Test
    void getLowStockProducts_WithNullThreshold_ShouldUseDefault() {
        // Given
        Product lowStockProduct = createLowStockProduct();
        List<Product> lowStockProducts = Arrays.asList(lowStockProduct);
        when(productRepository.findByStockGreaterThanEqual(1)).thenReturn(lowStockProducts);
        
        // When
        List<ProductResponseDTO> response = productService.getLowStockProducts(null);
        
        // Then
        assertEquals(1, response.size());
        verify(productRepository, times(1)).findByStockGreaterThanEqual(1);
    }
    
    @Test
    void getProductFilters_ShouldReturnAvailableFilters() {
        // Given
        when(productRepository.findDistinctCategories()).thenReturn(Arrays.asList("Sofas", "Tables"));
        when(productRepository.findDistinctMaterials()).thenReturn(Arrays.asList("Fabric", "Wood"));
        when(productRepository.findDistinctColors()).thenReturn(Arrays.asList("Blue", "Red"));
        
        // When
        Map<String, List<String>> filters = productService.getProductFilters();
        
        // Then
        assertEquals(3, filters.size());
        assertTrue(filters.containsKey("categories"));
        assertTrue(filters.containsKey("materials"));
        assertTrue(filters.containsKey("colors"));
        assertEquals(2, filters.get("categories").size());
    }
    
    @Test
    void isProductNameUnique_WithUniqueName_ShouldReturnTrue() {
        // Given
        when(productRepository.findByNameContainingIgnoreCase("Unique Product")).thenReturn(Collections.emptyList());
        
        // When
        boolean result = productService.isProductNameUnique("Unique Product", null);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void isProductNameUnique_WithExistingName_ShouldReturnFalse() {
        // Given
        List<Product> existingProducts = Arrays.asList(sampleProduct);
        when(productRepository.findByNameContainingIgnoreCase("Test Sofa")).thenReturn(existingProducts);
        
        // When
        boolean result = productService.isProductNameUnique("Test Sofa", null);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    void isProductNameUnique_WithExistingNameButExcludingSelf_ShouldReturnTrue() {
        // Given
        when(productRepository.findByNameContainingIgnoreCase("Test Sofa")).thenReturn(Arrays.asList(sampleProduct));
        
        // When
        boolean result = productService.isProductNameUnique("Test Sofa", 1L);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void isProductNameUnique_WithNullName_ShouldReturnFalse() {
        // When
        boolean result = productService.isProductNameUnique(null, null);
        
        // Then
        assertFalse(result);
        verify(productRepository, never()).findByNameContainingIgnoreCase(anyString());
    }
    
    @Test
    void isProductNameUnique_WithEmptyName_ShouldReturnFalse() {
        // When
        boolean result = productService.isProductNameUnique("   ", null);
        
        // Then
        assertFalse(result);
        verify(productRepository, never()).findByNameContainingIgnoreCase(anyString());
    }
    
    @Test
    void createProducts_WithValidRequests_ShouldReturnCreatedProducts() {
        // Given
        List<ProductRequestDTO> requests = Arrays.asList(validProductRequest, createSecondProductRequest());
        List<Product> savedProducts = Arrays.asList(sampleProduct, createSecondProduct());
        when(productRepository.saveAll(any())).thenReturn(savedProducts);
        
        // When
        List<ProductResponseDTO> response = productService.createProducts(requests);
        
        // Then
        assertEquals(2, response.size());
        verify(productRepository, times(1)).saveAll(any());
    }
    
    @Test
    void createProducts_WithInvalidRequest_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("");  // Invalid name
        List<ProductRequestDTO> requests = Arrays.asList(invalidRequest);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProducts(requests);
        });
        verify(productRepository, never()).saveAll(any());
    }
    
    @Test
    void validateProductRequest_WithNullName_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName(null);
        invalidRequest.setCategory("Test");
        invalidRequest.setPrice(new BigDecimal("100"));
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithEmptyName_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("   ");
        invalidRequest.setCategory("Test");
        invalidRequest.setPrice(new BigDecimal("100"));
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithNullPrice_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("Test Product");
        invalidRequest.setCategory("Test");
        invalidRequest.setPrice(null);
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithZeroPrice_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("Test Product");
        invalidRequest.setCategory("Test");
        invalidRequest.setPrice(BigDecimal.ZERO);
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithNegativePrice_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("Test Product");
        invalidRequest.setCategory("Test");
        invalidRequest.setPrice(new BigDecimal("-10"));
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithNullCategory_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("Test Product");
        invalidRequest.setCategory(null);
        invalidRequest.setPrice(new BigDecimal("100"));
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithEmptyCategory_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("Test Product");
        invalidRequest.setCategory("   ");
        invalidRequest.setPrice(new BigDecimal("100"));
        invalidRequest.setStock(10);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    @Test
    void validateProductRequest_WithNullStock_ShouldThrowException() {
        // Given
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("Test Product");
        invalidRequest.setCategory("Test");
        invalidRequest.setPrice(new BigDecimal("100"));
        invalidRequest.setStock(null);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(invalidRequest);
        });
    }
    
    // Helper methods for test data
    private Product createSecondProduct() {
        Product product = new Product();
        product.setId(2L);
        product.setName("Test Table");
        product.setCategory("Tables");
        product.setPrice(new BigDecimal("599.99"));
        product.setStock(15);
        product.setDescription("A test table");
        return product;
    }
    
    private ProductRequestDTO createSecondProductRequest() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Test Table");
        request.setCategory("Tables");
        request.setPrice(new BigDecimal("599.99"));
        request.setStock(15);
        request.setDescription("A test table");
        return request;
    }
    
    private Product createLowStockProduct() {
        Product product = new Product();
        product.setId(3L);
        product.setName("Low Stock Item");
        product.setCategory("Chairs");
        product.setPrice(new BigDecimal("299.99"));
        product.setStock(3); // Low stock
        product.setDescription("A low stock item");
        return product;
    }
} 