# Product Catalog Service

A RESTful API service for managing furniture products in an e-commerce platform. Built with Spring Boot and Java, this service provides comprehensive CRUD operations and advanced search capabilities for furniture inventory management.

## Features

- **Complete CRUD Operations**: Create, Read, Update, and Delete furniture products
- **Advanced Search**: Filter products by category, material, color, price range, and dimensions
- **Inventory Management**: Track stock levels, find low-stock items
- **Batch Operations**: Create multiple products at once
- **Data Validation**: Comprehensive input validation with meaningful error messages
- **In-Memory Database**: Uses H2 for easy development and testing
- **Sample Data**: Automatically loads 20+ furniture products on startup
- **CORS Enabled**: Ready for frontend integration

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (In-memory)
- **Gradle** (Build tool)
- **Lombok** (Reduces boilerplate code)
- **Jakarta Validation**

## Getting Started

### Prerequisites

- Java 17 (required)
- Gradle (included via wrapper)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd product-catalog-service
```

2. **Run the application:**

The project is configured to use Gradle's Java Toolchain for Java 17.
Gradle will auto-download a matching JDK if needed.

```bash
./gradlew bootRun
```

If you prefer to manage Java locally, you can also use the helper:
```bash
./run.sh
```

3. Build the project (optional):
```bash
./gradlew build  # Java 17 should already be active
```

The service will start on `http://localhost:8080/api`

### Accessing the H2 Console

The H2 database console is available at: `http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:furnituredb`
- **Username**: `sa`
- **Password**: `password`

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### 1. Create Product
```http
POST /products
Content-Type: application/json

{
  "name": "Modern Office Desk",
  "description": "Spacious desk with cable management",
  "category": "Tables",
  "price": 599.99,
  "stock": 10,
  "width": 160.0,
  "height": 75.0,
  "depth": 80.0,
  "material": "Oak wood",
  "color": "Natural",
  "imageUrl": "https://example.com/desk.jpg"
}
```

#### 2. Get All Products
```http
GET /products
```

#### 3. Get Product by ID
```http
GET /products/{id}
```

#### 4. Update Product
```http
PUT /products/{id}
Content-Type: application/json

{
  "name": "Updated Product Name",
  "price": 699.99,
  ...
}
```

#### 5. Delete Product
```http
DELETE /products/{id}
```

#### 6. Search Products
```http
GET /products/search?category=Sofas&minPrice=500&maxPrice=2000&inStock=true
```

Query Parameters:
- `category` (optional): Filter by category
- `material` (optional): Filter by material
- `color` (optional): Filter by color
- `minPrice` (optional): Minimum price
- `maxPrice` (optional): Maximum price
- `inStock` (optional): Show only in-stock items

#### 7. Get Products by Category
```http
GET /products/category/{category}
```

#### 8. Search by Name
```http
GET /products/search/name?query=office
```

#### 9. Get Products by Price Range
```http
GET /products/price-range?minPrice=100&maxPrice=1000
```

#### 10. Get Products by Dimensions
```http
GET /products/dimensions?maxWidth=200&maxHeight=100&maxDepth=80
```

#### 11. Update Stock
```http
PATCH /products/{id}/stock?quantity=25
```

#### 12. Get In-Stock Products
```http
GET /products/in-stock
```

#### 13. Get Low Stock Products
```http
GET /products/low-stock?threshold=5
```

#### 14. Get Available Filters
```http
GET /products/filters
```

Returns available categories, materials, and colors.

#### 15. Check Product Name Availability
```http
GET /products/check-name?name=New Product&excludeId=123
```

#### 16. Batch Create Products
```http
POST /products/batch
Content-Type: application/json

[
  {
    "name": "Product 1",
    ...
  },
  {
    "name": "Product 2",
    ...
  }
]
```

## Product Model

### Required Fields
- `name`: Product name (max 200 characters)
- `category`: Product category (max 100 characters)
- `price`: Product price (must be greater than 0)
- `stock`: Available quantity (cannot be negative)

### Optional Fields
- `description`: Detailed product description
- `width`, `height`, `depth`: Dimensions in centimeters
- `material`: Construction material
- `color`: Product color
- `imageUrl`: Product image URL

## Response Format

### Success Response
```json
{
  "id": 1,
  "name": "Luxe 3-Seater Sofa",
  "description": "A modern and comfortable sofa",
  "category": "Sofas",
  "price": 1299.99,
  "stock": 15,
  "dimensions": {
    "width": 210.0,
    "height": 85.0,
    "depth": 90.0,
    "unit": "cm"
  },
  "material": "Velvet upholstery",
  "color": "Navy Blue",
  "imageUrl": "https://example.com/sofa.jpg",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "inStock": true
}
```

### Error Response
```json
{
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

## Available Product Categories

- Sofas
- Chairs
- Tables
- Beds
- Storage

## Development

### Running Tests
```bash
./gradlew test
```

### Code Quality & Security Scanning

#### Checkstyle - Java Linting

This project uses [Checkstyle](https://checkstyle.org/) for Java code style and quality checks.

Run Checkstyle:
```bash
./checkstyle.sh
```

Or run individual checks:
```bash
./gradlew checkstyleMain    # Check main sources
./gradlew checkstyleTest    # Check test sources
./gradlew checkstyle        # Check all sources
```

Reports are generated at:
- Main: `build/reports/checkstyle/main.html`
- Test: `build/reports/checkstyle/test.html`

Configuration: `config/checkstyle/checkstyle.xml`

#### Semgrep - Security Scanning

This project uses [Semgrep](https://semgrep.dev/) for static analysis to detect security vulnerabilities and code quality issues.

Quick scan with recommended rules:
```bash
./semgrep.sh
```

Or run Semgrep directly:
```bash
# Scan with auto-detected rules (recommended)
semgrep scan --config=auto src/

# Scan with specific rulesets
semgrep scan --config=p/security-audit src/          # Security-focused
semgrep scan --config=p/java src/                    # Java best practices
semgrep scan --config=p/spring src/                  # Spring Boot specific
semgrep scan --config=p/owasp-top-ten src/          # OWASP Top 10

# Use custom rules only
semgrep scan --config=.semgrep.yml src/
```

Configuration:
- `.semgrep.yml`: Custom rules for Spring Boot and JPA best practices
- `.semgrepignore`: Files and directories to exclude from scanning

### Building JAR
```bash
./gradlew clean build
```

The JAR file will be created in `build/libs/`

### Configuration

Application properties can be modified in `src/main/resources/application.properties`

Key configurations:
- Server port: `8080`
- Context path: `/api`
- Database: H2 in-memory
- SQL logging: Enabled

## Sample Data

The application automatically loads sample furniture data on startup, including:
- 3 Sofas (ranging from $1,299 to $2,499)
- 4 Chairs (ranging from $179 to $549)
- 4 Tables (ranging from $399 to $1,199)
- 4 Beds (ranging from $799 to $1,999)
- 5 Storage items (ranging from $249 to $999)

## Error Handling

The API includes comprehensive error handling:
- `400 Bad Request`: Invalid input data
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Unexpected errors

All errors return a JSON response with error details.

## CORS Configuration

CORS is enabled for all origins to facilitate frontend development. In production, update the `@CrossOrigin` annotation in `ProductController.java` to specify allowed origins.

## License

This project is provided as-is for educational and development purposes. 