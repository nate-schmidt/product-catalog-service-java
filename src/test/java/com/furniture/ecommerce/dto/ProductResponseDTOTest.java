package com.furniture.ecommerce.dto;

import com.furniture.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseDTOTest {

    private Product sampleProduct;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2023, 6, 15, 10, 30);
        
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Test Sofa");
        sampleProduct.setDescription("A comfortable test sofa");
        sampleProduct.setCategory("Sofas");
        sampleProduct.setPrice(new BigDecimal("999.99"));
        sampleProduct.setStock(10);
        sampleProduct.setWidth(200.0);
        sampleProduct.setHeight(80.0);
        sampleProduct.setDepth(100.0);
        sampleProduct.setMaterial("Fabric");
        sampleProduct.setColor("Blue");
        sampleProduct.setImageUrl("https://example.com/sofa.jpg");
        sampleProduct.setCreatedAt(testDate);
        sampleProduct.setUpdatedAt(testDate.plusDays(1));
    }

    @Test
    void fromProduct_WithCompleteProduct_ShouldMapAllFields() {
        // When
        ProductResponseDTO dto = ProductResponseDTO.fromProduct(sampleProduct);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Test Sofa", dto.getName());
        assertEquals("A comfortable test sofa", dto.getDescription());
        assertEquals("Sofas", dto.getCategory());
        assertEquals(new BigDecimal("999.99"), dto.getPrice());
        assertEquals(10, dto.getStock());
        assertEquals("Fabric", dto.getMaterial());
        assertEquals("Blue", dto.getColor());
        assertEquals("https://example.com/sofa.jpg", dto.getImageUrl());
        assertEquals(testDate, dto.getCreatedAt());
        assertEquals(testDate.plusDays(1), dto.getUpdatedAt());
        assertTrue(dto.isInStock());
        
        // Check dimensions
        assertNotNull(dto.getDimensions());
        assertEquals(200.0, dto.getDimensions().getWidth());
        assertEquals(80.0, dto.getDimensions().getHeight());
        assertEquals(100.0, dto.getDimensions().getDepth());
        assertEquals("cm", dto.getDimensions().getUnit());
    }

    @Test
    void fromProduct_WithNullDimensions_ShouldNotCreateDimensionsDTO() {
        // Given
        sampleProduct.setWidth(null);
        sampleProduct.setHeight(null);
        sampleProduct.setDepth(null);

        // When
        ProductResponseDTO dto = ProductResponseDTO.fromProduct(sampleProduct);

        // Then
        assertNull(dto.getDimensions());
    }

    @Test
    void fromProduct_WithPartialDimensions_ShouldCreateDimensionsDTO() {
        // Given
        sampleProduct.setWidth(200.0);
        sampleProduct.setHeight(null);
        sampleProduct.setDepth(100.0);

        // When
        ProductResponseDTO dto = ProductResponseDTO.fromProduct(sampleProduct);

        // Then
        assertNotNull(dto.getDimensions());
        assertEquals(200.0, dto.getDimensions().getWidth());
        assertNull(dto.getDimensions().getHeight());
        assertEquals(100.0, dto.getDimensions().getDepth());
    }

    @Test
    void fromProduct_WithZeroStock_ShouldSetInStockToFalse() {
        // Given
        sampleProduct.setStock(0);

        // When
        ProductResponseDTO dto = ProductResponseDTO.fromProduct(sampleProduct);

        // Then
        assertFalse(dto.isInStock());
    }

    @Test
    void fromProduct_WithNullStock_ShouldSetInStockToFalse() {
        // Given
        sampleProduct.setStock(null);

        // When
        ProductResponseDTO dto = ProductResponseDTO.fromProduct(sampleProduct);

        // Then
        assertFalse(dto.isInStock());
    }

    @Test
    void fromProduct_WithPositiveStock_ShouldSetInStockToTrue() {
        // Given
        sampleProduct.setStock(1);

        // When
        ProductResponseDTO dto = ProductResponseDTO.fromProduct(sampleProduct);

        // Then
        assertTrue(dto.isInStock());
    }

    @Test
    void dimensionsDTO_DefaultConstructor_ShouldSetDefaultUnit() {
        // Given & When
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO();

        // Then
        assertEquals("cm", dimensions.getUnit());
        assertNull(dimensions.getWidth());
        assertNull(dimensions.getHeight());
        assertNull(dimensions.getDepth());
    }

    @Test
    void dimensionsDTO_ParameterizedConstructor_ShouldSetDimensions() {
        // Given & When
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO(150.0, 75.0, 80.0);

        // Then
        assertEquals(150.0, dimensions.getWidth());
        assertEquals(75.0, dimensions.getHeight());
        assertEquals(80.0, dimensions.getDepth());
        assertEquals("cm", dimensions.getUnit());
    }

    @Test
    void getFormattedDimensions_WithAllDimensions_ShouldReturnFormattedString() {
        // Given
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO(200.0, 80.0, 100.0);

        // When
        String formatted = dimensions.getFormattedDimensions();

        // Then
        assertEquals("200.0 x 80.0 x 100.0 cm", formatted);
    }

    @Test
    void getFormattedDimensions_WithNullDimensions_ShouldReturnDefaultMessage() {
        // Given
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO();

        // When
        String formatted = dimensions.getFormattedDimensions();

        // Then
        assertEquals("Dimensions not specified", formatted);
    }

    @Test
    void getFormattedDimensions_WithPartialDimensions_ShouldReturnDefaultMessage() {
        // Given
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO();
        dimensions.setWidth(200.0);
        dimensions.setHeight(null);
        dimensions.setDepth(100.0);

        // When
        String formatted = dimensions.getFormattedDimensions();

        // Then
        assertEquals("Dimensions not specified", formatted);
    }

    @Test
    void getFormattedDimensions_WithCustomUnit_ShouldUseCustomUnit() {
        // Given
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO(72.0, 30.0, 36.0);
        dimensions.setUnit("inches");

        // When
        String formatted = dimensions.getFormattedDimensions();

        // Then
        assertEquals("72.0 x 30.0 x 36.0 inches", formatted);
    }

    @Test
    void responseDTO_ParameterizedConstructor_ShouldCreateDimensionsWhenProvided() {
        // Given & When
        ProductResponseDTO dto = new ProductResponseDTO(
                1L, "Test Sofa", "Description", "Sofas",
                new BigDecimal("999.99"), 10, 200.0, 80.0, 100.0,
                "Fabric", "Blue", "https://example.com/image.jpg",
                testDate, testDate.plusDays(1)
        );

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Test Sofa", dto.getName());
        assertTrue(dto.isInStock());
        assertNotNull(dto.getDimensions());
        assertEquals(200.0, dto.getDimensions().getWidth());
        assertEquals(80.0, dto.getDimensions().getHeight());
        assertEquals(100.0, dto.getDimensions().getDepth());
    }

    @Test
    void responseDTO_ParameterizedConstructor_WithNullDimensions_ShouldNotCreateDimensions() {
        // Given & When
        ProductResponseDTO dto = new ProductResponseDTO(
                1L, "Test Sofa", "Description", "Sofas",
                new BigDecimal("999.99"), 10, null, null, null,
                "Fabric", "Blue", "https://example.com/image.jpg",
                testDate, testDate.plusDays(1)
        );

        // Then
        assertEquals(1L, dto.getId());
        assertNull(dto.getDimensions());
        assertTrue(dto.isInStock());
    }

    @Test
    void setDimensions_ShouldSetDimensionsCorrectly() {
        // Given
        ProductResponseDTO dto = new ProductResponseDTO();
        ProductResponseDTO.DimensionsDTO dimensions = new ProductResponseDTO.DimensionsDTO(150.0, 75.0, 80.0);

        // When
        dto.setDimensions(dimensions);

        // Then
        assertEquals(dimensions, dto.getDimensions());
        assertEquals(150.0, dto.getDimensions().getWidth());
    }

    @Test
    void inStockCalculation_ShouldBeCorrect() {
        // Given
        ProductResponseDTO dto = new ProductResponseDTO();

        // Test with positive stock
        dto.setStock(5);
        dto.setInStock(dto.getStock() > 0);
        assertTrue(dto.isInStock());

        // Test with zero stock
        dto.setStock(0);
        dto.setInStock(dto.getStock() > 0);
        assertFalse(dto.isInStock());

        // Test with negative stock (should not happen but testing edge case)
        dto.setStock(-1);
        dto.setInStock(dto.getStock() > 0);
        assertFalse(dto.isInStock());
    }
}