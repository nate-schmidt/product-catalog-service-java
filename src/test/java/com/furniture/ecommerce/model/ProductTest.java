package com.furniture.ecommerce.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Test Sofa");
        product.setDescription("A comfortable test sofa");
        product.setCategory("Sofas");
        product.setPrice(new BigDecimal("999.99"));
        product.setStock(10);
        product.setWidth(200.0);
        product.setHeight(80.0);
        product.setDepth(100.0);
        product.setMaterial("Fabric");
        product.setColor("Blue");
        product.setImageUrl("https://example.com/sofa.jpg");
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyProduct() {
        // Given & When
        Product newProduct = new Product();

        // Then
        assertNull(newProduct.getId());
        assertNull(newProduct.getName());
        assertNull(newProduct.getDescription());
        assertNull(newProduct.getCategory());
        assertNull(newProduct.getPrice());
        assertNull(newProduct.getStock());
        assertNull(newProduct.getCreatedAt());
        assertNull(newProduct.getUpdatedAt());
    }

    @Test
    void parameterizedConstructor_ShouldSetAllFields() {
        // Given & When
        Product product = new Product(
                "Test Table", "A wooden test table", "Tables",
                new BigDecimal("599.99"), 5,
                150.0, 75.0, 90.0,
                "Wood", "Brown", "https://example.com/table.jpg"
        );

        // Then
        assertEquals("Test Table", product.getName());
        assertEquals("A wooden test table", product.getDescription());
        assertEquals("Tables", product.getCategory());
        assertEquals(new BigDecimal("599.99"), product.getPrice());
        assertEquals(5, product.getStock());
        assertEquals(150.0, product.getWidth());
        assertEquals(75.0, product.getHeight());
        assertEquals(90.0, product.getDepth());
        assertEquals("Wood", product.getMaterial());
        assertEquals("Brown", product.getColor());
        assertEquals("https://example.com/table.jpg", product.getImageUrl());
    }

    @Test
    void onCreate_ShouldSetCreatedAtAndUpdatedAt() {
        // When - Simulating JPA @PrePersist
        product.onCreate();

        // Then
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
        // Both should be set to current time (within a few milliseconds of each other)
        assertTrue(Math.abs(product.getCreatedAt().getNano() - product.getUpdatedAt().getNano()) < 1000000); // Within 1ms
    }

    @Test
    void onCreate_WithExistingCreatedAt_ShouldStillSetBothTimestamps() {
        // Given
        LocalDateTime existingCreatedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        product.setCreatedAt(existingCreatedAt);

        // When
        product.onCreate();

        // Then
        // Based on the implementation, onCreate always sets both timestamps to current time
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
        // Both should be set to current time (within a few milliseconds of each other)
        assertTrue(Math.abs(product.getCreatedAt().getNano() - product.getUpdatedAt().getNano()) < 1000000); // Within 1ms
        // The timestamps should be current time, not the existing one
        assertNotEquals(existingCreatedAt, product.getCreatedAt());
    }

    @Test
    void onUpdate_ShouldUpdateUpdatedAtOnly() {
        // Given
        LocalDateTime originalCreatedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime originalUpdatedAt = LocalDateTime.of(2023, 1, 2, 12, 0);
        product.setCreatedAt(originalCreatedAt);
        product.setUpdatedAt(originalUpdatedAt);

        LocalDateTime beforeUpdate = LocalDateTime.now();

        // When - Simulating JPA @PreUpdate
        product.onUpdate();

        // Then
        LocalDateTime afterUpdate = LocalDateTime.now();
        assertEquals(originalCreatedAt, product.getCreatedAt()); // Should not change
        assertNotNull(product.getUpdatedAt());
        assertTrue(product.getUpdatedAt().isAfter(beforeUpdate) || product.getUpdatedAt().isEqual(beforeUpdate));
        assertTrue(product.getUpdatedAt().isBefore(afterUpdate) || product.getUpdatedAt().isEqual(afterUpdate));
        assertNotEquals(originalUpdatedAt, product.getUpdatedAt());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        Long id = 123L;
        BigDecimal newPrice = new BigDecimal("1299.99");
        Integer newStock = 15;

        // When
        product.setId(id);
        product.setPrice(newPrice);
        product.setStock(newStock);

        // Then
        assertEquals(id, product.getId());
        assertEquals(newPrice, product.getPrice());
        assertEquals(newStock, product.getStock());
    }

    @Test
    void setDimensions_ShouldAllowNullValues() {
        // When
        product.setWidth(null);
        product.setHeight(null);
        product.setDepth(null);

        // Then
        assertNull(product.getWidth());
        assertNull(product.getHeight());
        assertNull(product.getDepth());
    }

    @Test
    void setMaterialAndColor_ShouldAllowNullValues() {
        // When
        product.setMaterial(null);
        product.setColor(null);

        // Then
        assertNull(product.getMaterial());
        assertNull(product.getColor());
    }

    @Test
    void toString_ShouldReturnFormattedString() {
        // Given
        product.setId(1L);

        // When
        String result = product.toString();

        // Then
        assertTrue(result.contains("Product{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Test Sofa'"));
        assertTrue(result.contains("category='Sofas'"));
        assertTrue(result.contains("price=999.99"));
        assertTrue(result.contains("stock=10"));
        assertTrue(result.contains("width=200.0"));
        assertTrue(result.contains("height=80.0"));
        assertTrue(result.contains("depth=100.0"));
        assertTrue(result.contains("material='Fabric'"));
        assertTrue(result.contains("color='Blue'"));
    }

    @Test
    void toString_WithNullValues_ShouldHandleGracefully() {
        // Given
        Product emptyProduct = new Product();

        // When
        String result = emptyProduct.toString();

        // Then
        assertTrue(result.contains("Product{"));
        assertTrue(result.contains("id=null"));
        assertTrue(result.contains("name='null'"));
        assertTrue(result.contains("price=null"));
        assertTrue(result.contains("stock=null"));
    }

    @Test
    void priceComparison_ShouldWorkWithBigDecimal() {
        // Given
        BigDecimal price1 = new BigDecimal("999.99");
        BigDecimal price2 = new BigDecimal("999.99");
        BigDecimal price3 = new BigDecimal("1000.00");

        product.setPrice(price1);
        Product product2 = new Product();
        product2.setPrice(price2);
        Product product3 = new Product();
        product3.setPrice(price3);

        // Then
        assertEquals(0, product.getPrice().compareTo(product2.getPrice()));
        assertTrue(product.getPrice().compareTo(product3.getPrice()) < 0);
        assertTrue(product3.getPrice().compareTo(product.getPrice()) > 0);
    }
}