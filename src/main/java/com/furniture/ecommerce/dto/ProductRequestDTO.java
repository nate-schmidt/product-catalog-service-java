package com.furniture.ecommerce.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequestDTO {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;
    
    // Dimensions (optional for some products)
    @PositiveOrZero(message = "Width must be positive or zero")
    private Double width;
    
    @PositiveOrZero(message = "Height must be positive or zero")
    private Double height;
    
    @PositiveOrZero(message = "Depth must be positive or zero")
    private Double depth;
    
    @Size(max = 100, message = "Material must not exceed 100 characters")
    private String material;
    
    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;
    
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Pattern(regexp = "^(https?://.*)?(.*\\.(jpg|jpeg|png|gif|webp))$", 
             message = "Invalid image URL format", 
             flags = Pattern.Flag.CASE_INSENSITIVE)
    private String imageUrl;
    
    // Constructors
    public ProductRequestDTO() {
    }
    
    public ProductRequestDTO(String name, String description, String category, 
                           BigDecimal price, Integer stock, Double width, 
                           Double height, Double depth, String material, 
                           String color, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.material = material;
        this.color = color;
        this.imageUrl = imageUrl;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Double getWidth() {
        return width;
    }
    
    public void setWidth(Double width) {
        this.width = width;
    }
    
    public Double getHeight() {
        return height;
    }
    
    public void setHeight(Double height) {
        this.height = height;
    }
    
    public Double getDepth() {
        return depth;
    }
    
    public void setDepth(Double depth) {
        this.depth = depth;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public void setMaterial(String material) {
        this.material = material;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
} 