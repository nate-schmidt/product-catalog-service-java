package com.furniture.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furniture.ecommerce.dto.ProductRequestDTO;
import com.furniture.ecommerce.dto.ProductResponseDTO;
import com.furniture.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequestDTO validProductRequest;
    private ProductResponseDTO sampleProductResponse;

    @BeforeEach
    void setUp() {
        validProductRequest = new ProductRequestDTO();
        validProductRequest.setName("Test Sofa");
        validProductRequest.setCategory("Sofas");
        validProductRequest.setPrice(new BigDecimal("999.99"));
        validProductRequest.setStock(10);
        validProductRequest.setDescription("A test sofa");

        sampleProductResponse = new ProductResponseDTO();
        sampleProductResponse.setId(1L);
        sampleProductResponse.setName("Test Sofa");
        sampleProductResponse.setCategory("Sofas");
        sampleProductResponse.setPrice(new BigDecimal("999.99"));
        sampleProductResponse.setStock(10);
        sampleProductResponse.setDescription("A test sofa");
        sampleProductResponse.setCreatedAt(LocalDateTime.now());
        sampleProductResponse.setUpdatedAt(LocalDateTime.now());
        sampleProductResponse.setInStock(true);
    }

    @Test
    void createProduct_WithValidRequest_ShouldReturnCreatedProduct() throws Exception {
        // Given
        when(productService.createProduct(any(ProductRequestDTO.class))).thenReturn(sampleProductResponse);

        // When & Then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Sofa"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void getProductById_WithValidId_ShouldReturnProduct() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(sampleProductResponse);

        // When & Then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Sofa"));
    }

    @Test
    void getProductById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.getProductById(999L)).thenThrow(new NoSuchElementException("Product not found"));

        // When & Then
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        List<ProductResponseDTO> products = Arrays.asList(sampleProductResponse);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateProduct_WithValidRequest_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        when(productService.updateProduct(eq(1L), any(ProductRequestDTO.class))).thenReturn(sampleProductResponse);

        // When & Then
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteProduct_WithValidId_ShouldReturnSuccessMessage() throws Exception {
        // When & Then
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product deleted successfully"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void searchProducts_WithFilters_ShouldReturnFilteredProducts() throws Exception {
        // Given
        List<ProductResponseDTO> filteredProducts = Arrays.asList(sampleProductResponse);
        when(productService.searchProducts(eq("Sofas"), eq("Fabric"), eq("Blue"), 
                any(BigDecimal.class), any(BigDecimal.class), eq(true)))
                .thenReturn(filteredProducts);

        // When & Then
        mockMvc.perform(get("/products/search")
                .param("category", "Sofas")
                .param("material", "Fabric")
                .param("color", "Blue")
                .param("minPrice", "500")
                .param("maxPrice", "1500")
                .param("inStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getProductsByCategory_ShouldReturnCategoryProducts() throws Exception {
        // Given
        List<ProductResponseDTO> sofas = Arrays.asList(sampleProductResponse);
        when(productService.getProductsByCategory("Sofas")).thenReturn(sofas);

        // When & Then
        mockMvc.perform(get("/products/category/Sofas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void searchByName_ShouldReturnMatchingProducts() throws Exception {
        // Given
        List<ProductResponseDTO> products = Arrays.asList(sampleProductResponse);
        when(productService.searchByName("sofa")).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/products/search/name")
                .param("query", "sofa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getProductsByPriceRange_ShouldReturnProductsInRange() throws Exception {
        // Given
        List<ProductResponseDTO> products = Arrays.asList(sampleProductResponse);
        when(productService.getProductsByPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(products);

        // When & Then
        mockMvc.perform(get("/products/price-range")
                .param("minPrice", "500")
                .param("maxPrice", "1500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateStock_WithValidQuantity_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        sampleProductResponse.setStock(25);
        when(productService.updateStock(1L, 25)).thenReturn(sampleProductResponse);

        // When & Then
        mockMvc.perform(patch("/products/1/stock")
                .param("quantity", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(25));
    }

    @Test
    void getInStockProducts_ShouldReturnInStockProducts() throws Exception {
        // Given
        List<ProductResponseDTO> inStockProducts = Arrays.asList(sampleProductResponse);
        when(productService.getInStockProducts()).thenReturn(inStockProducts);

        // When & Then
        mockMvc.perform(get("/products/in-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getProductFilters_ShouldReturnAvailableFilters() throws Exception {
        // Given
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("categories", Arrays.asList("Sofas", "Tables"));
        filters.put("materials", Arrays.asList("Fabric", "Wood"));
        filters.put("colors", Arrays.asList("Blue", "Red"));
        when(productService.getProductFilters()).thenReturn(filters);

        // When & Then
        mockMvc.perform(get("/products/filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.materials").isArray())
                .andExpect(jsonPath("$.colors").isArray());
    }

    @Test
    void checkProductName_WithAvailableName_ShouldReturnTrue() throws Exception {
        // Given
        when(productService.isProductNameUnique("New Product", null)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/products/check-name")
                .param("name", "New Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void createProducts_WithValidRequests_ShouldReturnCreatedProducts() throws Exception {
        // Given
        List<ProductRequestDTO> requests = Arrays.asList(validProductRequest);
        List<ProductResponseDTO> responses = Arrays.asList(sampleProductResponse);
        when(productService.createProducts(any())).thenReturn(responses);

        // When & Then
        mockMvc.perform(post("/products/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createProduct_WithServiceException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(productService.createProduct(any(ProductRequestDTO.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void createProduct_WithValidationException_ShouldReturnBadRequest() throws Exception {
        // Given
        when(productService.createProduct(any(ProductRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Product name is required"));

        // When & Then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}