package com.furniture.ecommerce.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestDTOTest {

    private static Validator validator;
    private ProductRequestDTO validProduct;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        validProduct = new ProductRequestDTO();
        validProduct.setName("Test Sofa");
        validProduct.setDescription("A comfortable test sofa");
        validProduct.setCategory("Sofas");
        validProduct.setPrice(new BigDecimal("999.99"));
        validProduct.setStock(10);
        validProduct.setWidth(200.0);
        validProduct.setHeight(80.0);
        validProduct.setDepth(100.0);
        validProduct.setMaterial("Fabric");
        validProduct.setColor("Blue");
        validProduct.setImageUrl("https://example.com/sofa.jpg");
    }

    @Test
    void validProduct_ShouldPassValidation() {
        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullName_ShouldFailValidation() {
        // Given
        validProduct.setName(null);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Product name is required")));
    }

    @Test
    void emptyName_ShouldFailValidation() {
        // Given
        validProduct.setName("");

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Product name is required")));
    }

    @Test
    void blankName_ShouldFailValidation() {
        // Given
        validProduct.setName("   ");

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Product name is required")));
    }

    @Test
    void nameExceedingMaxLength_ShouldFailValidation() {
        // Given
        String longName = "A".repeat(201); // Exceeds 200 character limit
        validProduct.setName(longName);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 200 characters")));
    }

    @Test
    void nullPrice_ShouldFailValidation() {
        // Given
        validProduct.setPrice(null);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price is required")));
    }

    @Test
    void zeroPrice_ShouldFailValidation() {
        // Given
        validProduct.setPrice(BigDecimal.ZERO);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than 0")));
    }

    @Test
    void negativePrice_ShouldFailValidation() {
        // Given
        validProduct.setPrice(new BigDecimal("-10.00"));

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than 0")));
    }

    @Test
    void priceTooManyIntegerDigits_ShouldFailValidation() {
        // Given
        validProduct.setPrice(new BigDecimal("123456789.99")); // 9 integer digits, limit is 8

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("at most 8 integer digits")));
    }

    @Test
    void priceTooManyFractionDigits_ShouldFailValidation() {
        // Given
        validProduct.setPrice(new BigDecimal("999.999")); // 3 fraction digits, limit is 2

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("2 decimal places")));
    }

    @Test
    void nullStock_ShouldFailValidation() {
        // Given
        validProduct.setStock(null);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Stock quantity is required")));
    }

    @Test
    void negativeStock_ShouldFailValidation() {
        // Given
        validProduct.setStock(-1);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Stock cannot be negative")));
    }

    @Test
    void zeroStock_ShouldPassValidation() {
        // Given
        validProduct.setStock(0);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullCategory_ShouldFailValidation() {
        // Given
        validProduct.setCategory(null);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Category is required")));
    }

    @Test
    void blankCategory_ShouldFailValidation() {
        // Given
        validProduct.setCategory("   ");

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Category is required")));
    }

    @Test
    void categoryExceedingMaxLength_ShouldFailValidation() {
        // Given
        String longCategory = "A".repeat(101); // Exceeds 100 character limit
        validProduct.setCategory(longCategory);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 100 characters")));
    }

    @Test
    void descriptionExceedingMaxLength_ShouldFailValidation() {
        // Given
        String longDescription = "A".repeat(5001); // Exceeds 5000 character limit
        validProduct.setDescription(longDescription);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 5000 characters")));
    }

    @Test
    void negativeDimensions_ShouldFailValidation() {
        // Given
        validProduct.setWidth(-10.0);
        validProduct.setHeight(-5.0);
        validProduct.setDepth(-15.0);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(3, violations.size());
        assertTrue(violations.stream().allMatch(v -> v.getMessage().contains("must be positive or zero")));
    }

    @Test
    void zeroDimensions_ShouldPassValidation() {
        // Given
        validProduct.setWidth(0.0);
        validProduct.setHeight(0.0);
        validProduct.setDepth(0.0);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void materialExceedingMaxLength_ShouldFailValidation() {
        // Given
        String longMaterial = "A".repeat(101); // Exceeds 100 character limit
        validProduct.setMaterial(longMaterial);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 100 characters")));
    }

    @Test
    void colorExceedingMaxLength_ShouldFailValidation() {
        // Given
        String longColor = "A".repeat(51); // Exceeds 50 character limit
        validProduct.setColor(longColor);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 50 characters")));
    }

    @Test
    void invalidImageUrl_ShouldFailValidation() {
        // Given
        validProduct.setImageUrl("not-a-valid-url");

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid image URL format")));
    }

    @Test
    void validImageUrls_ShouldPassValidation() {
        // Test various valid image URL formats
        String[] validUrls = {
                "https://example.com/image.jpg",
                "http://example.com/image.jpeg", 
                "https://cdn.example.com/path/to/image.png",
                "https://example.com/image.gif",
                "https://example.com/image.webp",
                "image.jpg", // relative path
                "path/to/image.PNG" // uppercase extension
        };

        for (String url : validUrls) {
            validProduct.setImageUrl(url);
            Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);
            assertTrue(violations.isEmpty(), "URL should be valid: " + url);
        }
    }

    @Test
    void imageUrlExceedingMaxLength_ShouldFailValidation() {
        // Given
        String longUrl = "https://example.com/" + "A".repeat(500) + ".jpg"; // Exceeds 500 character limit
        validProduct.setImageUrl(longUrl);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 500 characters")));
    }

    @Test
    void nullOptionalFields_ShouldPassValidation() {
        // Given
        validProduct.setDescription(null);
        validProduct.setWidth(null);
        validProduct.setHeight(null);
        validProduct.setDepth(null);
        validProduct.setMaterial(null);
        validProduct.setColor(null);
        validProduct.setImageUrl(null);

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void parameterizedConstructor_ShouldSetAllFields() {
        // Given & When
        ProductRequestDTO dto = new ProductRequestDTO(
                "Test Table", "A wooden table", "Tables",
                new BigDecimal("599.99"), 5, 150.0, 75.0, 90.0,
                "Wood", "Brown", "https://example.com/table.jpg"
        );

        // Then
        assertEquals("Test Table", dto.getName());
        assertEquals("A wooden table", dto.getDescription());
        assertEquals("Tables", dto.getCategory());
        assertEquals(new BigDecimal("599.99"), dto.getPrice());
        assertEquals(5, dto.getStock());
        assertEquals(150.0, dto.getWidth());
        assertEquals(75.0, dto.getHeight());
        assertEquals(90.0, dto.getDepth());
        assertEquals("Wood", dto.getMaterial());
        assertEquals("Brown", dto.getColor());
        assertEquals("https://example.com/table.jpg", dto.getImageUrl());
    }

    @Test
    void multipleValidationErrors_ShouldReturnAllViolations() {
        // Given
        ProductRequestDTO invalidProduct = new ProductRequestDTO();
        invalidProduct.setName(""); // blank name
        invalidProduct.setCategory(""); // blank category
        invalidProduct.setPrice(BigDecimal.ZERO); // zero price
        invalidProduct.setStock(-1); // negative stock
        invalidProduct.setWidth(-10.0); // negative width

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(invalidProduct);

        // Then
        assertEquals(5, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Product name is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Category is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Stock cannot be negative")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Width must be positive or zero")));
    }

    @Test
    void edgeCaseValues_ShouldPassValidation() {
        // Given - testing edge cases that should be valid
        validProduct.setPrice(new BigDecimal("0.01")); // minimum valid price
        validProduct.setStock(0); // minimum valid stock
        validProduct.setWidth(0.0); // minimum valid dimension
        validProduct.setName("A"); // minimum length name
        validProduct.setCategory("X"); // minimum length category

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void maximumValidValues_ShouldPassValidation() {
        // Given - testing maximum allowed values
        validProduct.setName("A".repeat(200)); // max length name
        validProduct.setCategory("B".repeat(100)); // max length category
        validProduct.setDescription("C".repeat(5000)); // max length description
        validProduct.setMaterial("D".repeat(100)); // max length material
        validProduct.setColor("E".repeat(50)); // max length color
        validProduct.setImageUrl("https://example.com/" + "F".repeat(470) + ".jpg"); // max length URL
        validProduct.setPrice(new BigDecimal("99999999.99")); // max valid price (8 integer digits)

        // When
        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(validProduct);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Given
        ProductRequestDTO dto = new ProductRequestDTO();
        BigDecimal price = new BigDecimal("1299.99");
        
        // When
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setCategory("Test Category");
        dto.setPrice(price);
        dto.setStock(20);
        dto.setWidth(100.0);
        dto.setHeight(50.0);
        dto.setDepth(75.0);
        dto.setMaterial("Test Material");
        dto.setColor("Test Color");
        dto.setImageUrl("https://example.com/test.jpg");

        // Then
        assertEquals("Test Product", dto.getName());
        assertEquals("Test Description", dto.getDescription());
        assertEquals("Test Category", dto.getCategory());
        assertEquals(price, dto.getPrice());
        assertEquals(20, dto.getStock());
        assertEquals(100.0, dto.getWidth());
        assertEquals(50.0, dto.getHeight());
        assertEquals(75.0, dto.getDepth());
        assertEquals("Test Material", dto.getMaterial());
        assertEquals("Test Color", dto.getColor());
        assertEquals("https://example.com/test.jpg", dto.getImageUrl());
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyDTO() {
        // Given & When
        ProductRequestDTO dto = new ProductRequestDTO();

        // Then
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getCategory());
        assertNull(dto.getPrice());
        assertNull(dto.getStock());
        assertNull(dto.getWidth());
        assertNull(dto.getHeight());
        assertNull(dto.getDepth());
        assertNull(dto.getMaterial());
        assertNull(dto.getColor());
        assertNull(dto.getImageUrl());
    }
}