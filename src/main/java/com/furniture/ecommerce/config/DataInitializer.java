package com.furniture.ecommerce.config;

import com.furniture.ecommerce.model.Product;
import com.furniture.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("!test") // Don't run during tests
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (productRepository.count() > 0) {
            logger.info("Data already initialized, skipping...");
            return;
        }

        logger.info("Initializing sample furniture data...");

        List<Product> products = Arrays.asList(
            // Sofas
            new Product(
                "Luxe 3-Seater Sofa",
                "A modern and comfortable 3-seater sofa with plush cushions and sturdy wooden frame. Perfect for any living room.",
                "Sofas",
                new BigDecimal("1299.99"),
                15,
                210.0, 85.0, 90.0,
                "Velvet upholstery, Oak wood frame",
                "Navy Blue",
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&h=300&fit=crop"
            ),
            new Product(
                "Scandinavian L-Shape Sectional",
                "Minimalist L-shaped sectional sofa with clean lines and neutral tones. Features reversible chaise.",
                "Sofas",
                new BigDecimal("1899.99"),
                8,
                280.0, 82.0, 160.0,
                "Linen fabric, Pine wood frame",
                "Light Gray",
                "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400&h=300&fit=crop"
            ),
            new Product(
                "Classic Chesterfield Sofa",
                "Timeless Chesterfield design with tufted leather upholstery and rolled arms. A statement piece for any room.",
                "Sofas",
                new BigDecimal("2499.99"),
                5,
                220.0, 75.0, 95.0,
                "Genuine leather, Hardwood frame",
                "Cognac Brown",
                "https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=400&h=300&fit=crop"
            ),

            // Chairs
            new Product(
                "Ergonomic Office Chair",
                "High-back office chair with lumbar support, adjustable height, and breathable mesh back.",
                "Chairs",
                new BigDecimal("399.99"),
                25,
                65.0, 120.0, 65.0,
                "Mesh fabric, Aluminum base",
                "Black",
                "https://images.unsplash.com/photo-1541558869434-2840d308329a?w=400&h=300&fit=crop"
            ),
            new Product(
                "Mid-Century Accent Chair",
                "Stylish accent chair with tapered legs and button-tufted backrest. Perfect for reading corners.",
                "Chairs",
                new BigDecimal("549.99"),
                18,
                75.0, 80.0, 70.0,
                "Velvet upholstery, Walnut wood legs",
                "Emerald Green",
                "https://images.unsplash.com/photo-1506439773649-6e0eb8cfb237?w=400&h=300&fit=crop"
            ),
            new Product(
                "Windsor Dining Chair Set (2)",
                "Traditional Windsor-style dining chairs with spindle backs. Sold as a set of 2.",
                "Chairs",
                new BigDecimal("299.99"),
                30,
                45.0, 95.0, 50.0,
                "Solid Oak wood",
                "Natural Wood",
                "https://images.unsplash.com/photo-1549497538-303791108f95?w=400&h=300&fit=crop"
            ),
            new Product(
                "Modern Swivel Bar Stool",
                "Contemporary bar stool with 360-degree swivel and adjustable height. Chrome footrest.",
                "Chairs",
                new BigDecimal("179.99"),
                20,
                40.0, 110.0, 40.0,
                "Faux leather, Chrome steel",
                "White",
                "https://images.unsplash.com/photo-1503602642458-232111445657?w=400&h=300&fit=crop"
            ),

            // Tables
            new Product(
                "Expandable Dining Table",
                "Solid wood dining table that extends from 6 to 8 seats. Features butterfly leaf extension.",
                "Tables",
                new BigDecimal("1199.99"),
                10,
                180.0, 76.0, 90.0,
                "Solid Mahogany wood",
                "Dark Walnut",
                "https://images.unsplash.com/photo-1449247709967-d4461a6a6103?w=400&h=300&fit=crop"
            ),
            new Product(
                "Glass Coffee Table",
                "Modern coffee table with tempered glass top and chrome legs. Includes lower shelf for storage.",
                "Tables",
                new BigDecimal("449.99"),
                15,
                120.0, 45.0, 60.0,
                "Tempered glass, Chrome steel",
                "Clear/Chrome",
                "https://images.unsplash.com/photo-1555646199-1ba4acbfd7c0?w=400&h=300&fit=crop"
            ),
            new Product(
                "Rustic Console Table",
                "Farmhouse-style console table with two drawers and open shelf. Perfect for entryways.",
                "Tables",
                new BigDecimal("399.99"),
                12,
                120.0, 80.0, 35.0,
                "Reclaimed Pine wood",
                "Distressed White",
                "https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=400&h=300&fit=crop"
            ),
            new Product(
                "Adjustable Standing Desk",
                "Electric height-adjustable desk with memory settings. Promotes healthy work habits.",
                "Tables",
                new BigDecimal("699.99"),
                20,
                150.0, 120.0, 75.0,
                "Bamboo top, Steel frame",
                "Natural/Black",
                "https://images.unsplash.com/photo-1541558869434-2840d308329a?w=400&h=300&fit=crop"
            ),

            // Beds
            new Product(
                "King Size Platform Bed",
                "Modern platform bed with upholstered headboard and built-in USB charging ports.",
                "Beds",
                new BigDecimal("1599.99"),
                8,
                193.0, 120.0, 213.0,
                "Linen upholstery, Solid wood frame",
                "Charcoal Gray",
                "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=400&h=300&fit=crop"
            ),
            new Product(
                "Queen Storage Bed Frame",
                "Space-saving bed frame with four built-in drawers for under-bed storage.",
                "Beds",
                new BigDecimal("1299.99"),
                10,
                153.0, 90.0, 203.0,
                "Engineered wood",
                "Espresso",
                "https://images.unsplash.com/photo-1540932239986-30128078f3c5?w=400&h=300&fit=crop"
            ),
            new Product(
                "Twin Bunk Bed",
                "Sturdy twin-over-twin bunk bed with ladder and safety rails. Perfect for kids' rooms.",
                "Beds",
                new BigDecimal("799.99"),
                6,
                99.0, 165.0, 203.0,
                "Solid Pine wood",
                "White",
                "https://images.unsplash.com/photo-1571508601382-5deb4ac9ce62?w=400&h=300&fit=crop"
            ),
            new Product(
                "Adjustable Base Bed Frame",
                "Electric adjustable bed base with wireless remote. Head and foot adjustment.",
                "Beds",
                new BigDecimal("1999.99"),
                5,
                153.0, 40.0, 203.0,
                "Steel frame, Memory foam compatible",
                "Black",
                "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=400&h=300&fit=crop"
            ),

            // Storage
            new Product(
                "6-Drawer Double Dresser",
                "Spacious dresser with six deep drawers and smooth-gliding metal runners.",
                "Storage",
                new BigDecimal("799.99"),
                12,
                150.0, 80.0, 50.0,
                "Solid wood, Metal hardware",
                "Natural Oak",
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&h=300&fit=crop"
            ),
            new Product(
                "Modular Bookshelf System",
                "5-tier bookshelf that can be configured horizontally or vertically. Includes back panel.",
                "Storage",
                new BigDecimal("299.99"),
                20,
                80.0, 180.0, 30.0,
                "Particleboard with laminate",
                "White",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=300&fit=crop"
            ),
            new Product(
                "Ottoman Storage Bench",
                "Upholstered storage ottoman that doubles as extra seating. Hinged lid for easy access.",
                "Storage",
                new BigDecimal("249.99"),
                15,
                120.0, 45.0, 45.0,
                "Faux leather, MDF",
                "Brown",
                "https://images.unsplash.com/photo-1506439773649-6e0eb8cfb237?w=400&h=300&fit=crop"
            ),
            new Product(
                "Wardrobe Armoire",
                "Two-door wardrobe with hanging rod, adjustable shelves, and drawer at bottom.",
                "Storage",
                new BigDecimal("999.99"),
                7,
                100.0, 200.0, 60.0,
                "Engineered wood",
                "White",
                "https://images.unsplash.com/photo-1558618644-fcd25c85cd64?w=400&h=300&fit=crop"
            ),
            new Product(
                "Industrial Storage Cabinet",
                "Metal storage cabinet with mesh doors and adjustable shelves. Lock included.",
                "Storage",
                new BigDecimal("449.99"),
                10,
                90.0, 180.0, 45.0,
                "Powder-coated steel",
                "Matte Black",
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&h=300&fit=crop"
            )
        );

        // Save all products
        List<Product> savedProducts = productRepository.saveAll(products);
        logger.info("Successfully initialized {} furniture products", savedProducts.size());
    }
} 