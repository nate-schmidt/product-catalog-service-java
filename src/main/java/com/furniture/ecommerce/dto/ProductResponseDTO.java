package com.furniture.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private DimensionsDTO dimensions;
    private String material;
    private String color;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean inStock;
    
    // Nested DTO for dimensions
    public static class DimensionsDTO {
        private Double width;
        private Double height;
        private Double depth;
        private String unit = "cm"; // Default unit
        
        public DimensionsDTO() {
        }
        
        public DimensionsDTO(Double width, Double height, Double depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
        }
        
        // Getters and Setters
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
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        public String getFormattedDimensions() {
            if (width != null && height != null && depth != null) {
                return String.format("%.1f x %.1f x %.1f %s", width, height, depth, unit);
            }
            return "Dimensions not specified";
        }
    }
    
    // Constructors
    public ProductResponseDTO() {
    }
    
    public ProductResponseDTO(Long id, String name, String description, String category,
                            BigDecimal price, Integer stock, Double width, Double height,
                            Double depth, String material, String color, String imageUrl,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        if (width != null || height != null || depth != null) {
            this.dimensions = new DimensionsDTO(width, height, depth);
        }
        this.material = material;
        this.color = color;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.inStock = stock != null && stock > 0;
    }
    
    // Factory method for creating from Product entity
    public static ProductResponseDTO fromProduct(com.furniture.ecommerce.model.Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        
        if (product.getWidth() != null || product.getHeight() != null || product.getDepth() != null) {
            dto.setDimensions(new DimensionsDTO(product.getWidth(), product.getHeight(), product.getDepth()));
        }
        
        dto.setMaterial(product.getMaterial());
        dto.setColor(product.getColor());
        dto.setImageUrl(product.getImageUrl());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setInStock(product.getStock() != null && product.getStock() > 0);
        
        return dto;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public DimensionsDTO getDimensions() {
        return dimensions;
    }
    
    public void setDimensions(DimensionsDTO dimensions) {
        this.dimensions = dimensions;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isInStock() {
        return inStock;
    }
    
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
} 
