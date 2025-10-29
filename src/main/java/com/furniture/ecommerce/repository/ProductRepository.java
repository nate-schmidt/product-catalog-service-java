package com.furniture.ecommerce.repository;

import com.furniture.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by category
    List<Product> findByCategory(String category);
    
    // Find products by category ignoring case
    List<Product> findByCategoryIgnoreCase(String category);
    
    // Find products by material
    List<Product> findByMaterial(String material);
    
    // Find products by color
    List<Product> findByColor(String color);
    
    // Find products by price range
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find products with stock greater than specified amount
    List<Product> findByStockGreaterThan(Integer stock);
    
    // Find in-stock products
    List<Product> findByStockGreaterThanEqual(Integer stock);
    
    // Find products by name containing (case-insensitive search)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products by multiple criteria using custom query
    @Query("SELECT p FROM Product p WHERE "
           + "(:category IS NULL OR LOWER(p.category) = LOWER(:category)) AND "
           + "(:material IS NULL OR LOWER(p.material) = LOWER(:material)) AND "
           + "(:color IS NULL OR LOWER(p.color) = LOWER(:color)) AND "
           + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
           + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
           + "(:inStock IS NULL OR (CASE WHEN :inStock = true THEN p.stock > 0 ELSE true END))")
    List<Product> searchProducts(@Param("category") String category,
                                @Param("material") String material,
                                @Param("color") String color,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("inStock") Boolean inStock);
    
    // Find products within specific dimensions
    @Query("SELECT p FROM Product p WHERE "
           + "(:maxWidth IS NULL OR p.width <= :maxWidth) AND "
           + "(:maxHeight IS NULL OR p.height <= :maxHeight) AND "
           + "(:maxDepth IS NULL OR p.depth <= :maxDepth)")
    List<Product> findByMaxDimensions(@Param("maxWidth") Double maxWidth,
                                     @Param("maxHeight") Double maxHeight,
                                     @Param("maxDepth") Double maxDepth);
    
    // Get distinct categories
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findDistinctCategories();
    
    // Get distinct materials
    @Query("SELECT DISTINCT p.material FROM Product p WHERE p.material IS NOT NULL ORDER BY p.material")
    List<String> findDistinctMaterials();
    
    // Get distinct colors
    @Query("SELECT DISTINCT p.color FROM Product p WHERE p.color IS NOT NULL ORDER BY p.color")
    List<String> findDistinctColors();
    
    // Check if product name already exists (for validation)
    boolean existsByNameIgnoreCase(String name);
    
    // Find products updated after a certain date
    List<Product> findByUpdatedAtAfter(java.time.LocalDateTime date);
} 
