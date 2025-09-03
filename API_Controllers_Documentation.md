# API Controllers Documentation

This document provides comprehensive documentation for all REST controllers in the Furniture E-commerce Product Catalog Service.

## Table of Contents

1. [Product Controller](#product-controller)
2. [API Response Format](#api-response-format)
3. [Error Handling](#error-handling)

---

## Product Controller

**Base Path:** `/products`  
**Description:** Manages furniture product catalog operations including CRUD operations, search functionality, and inventory management.

### Endpoints

#### 1. Create Product
- **Method:** `POST`
- **Path:** `/products`
- **Description:** Creates a new product in the catalog
- **Request Body:** `ProductRequestDTO` (JSON)
- **Response:** `ProductResponseDTO` with HTTP 201 (Created)
- **Validation:** Request body is validated using `@Valid`

#### 2. Get Product by ID
- **Method:** `GET`
- **Path:** `/products/{id}`
- **Description:** Retrieves a specific product by its ID
- **Path Parameters:**
  - `id` (Long, required): Product ID (minimum value: 1)
- **Response:** `ProductResponseDTO` with HTTP 200 (OK)

#### 3. Get All Products
- **Method:** `GET`
- **Path:** `/products`
- **Description:** Retrieves all products in the catalog
- **Response:** List of `ProductResponseDTO` with HTTP 200 (OK)

#### 4. Update Product
- **Method:** `PUT`
- **Path:** `/products/{id}`
- **Description:** Updates an existing product with new information
- **Path Parameters:**
  - `id` (Long, required): Product ID (minimum value: 1)
- **Request Body:** `ProductRequestDTO` (JSON)
- **Response:** `ProductResponseDTO` with HTTP 200 (OK)
- **Validation:** Request body is validated using `@Valid`

#### 5. Delete Product
- **Method:** `DELETE`
- **Path:** `/products/{id}`
- **Description:** Permanently removes a product from the catalog
- **Path Parameters:**
  - `id` (Long, required): Product ID (minimum value: 1)
- **Response:** JSON object with confirmation message and deleted product ID

**Response Format:**
```json
{
  "message": "Product deleted successfully",
  "id": "123"
}
```

#### 6. Search Products (Advanced)
- **Method:** `GET`
- **Path:** `/products/search`
- **Description:** Search products with multiple filter criteria
- **Query Parameters:** (all optional)
  - `category` (String): Filter by product category
  - `material` (String): Filter by material type
  - `color` (String): Filter by color
  - `minPrice` (BigDecimal): Minimum price filter
  - `maxPrice` (BigDecimal): Maximum price filter
  - `inStock` (Boolean): Filter by stock availability
- **Response:** List of `ProductResponseDTO` matching the criteria

#### 7. Get Products by Category
- **Method:** `GET`
- **Path:** `/products/category/{category}`
- **Description:** Retrieves all products in a specific category
- **Path Parameters:**
  - `category` (String, required): Product category name
- **Response:** List of `ProductResponseDTO` with HTTP 200 (OK)

#### 8. Search Products by Name
- **Method:** `GET`
- **Path:** `/products/search/name`
- **Description:** Search products by name using a query string
- **Query Parameters:**
  - `query` (String, required): Search term for product name
- **Response:** List of `ProductResponseDTO` matching the search query

#### 9. Get Products by Price Range
- **Method:** `GET`
- **Path:** `/products/price-range`
- **Description:** Filter products within a specified price range
- **Query Parameters:** (both optional)
  - `minPrice` (BigDecimal): Minimum price
  - `maxPrice` (BigDecimal): Maximum price
- **Response:** List of `ProductResponseDTO` within the price range

#### 10. Get Products by Dimensions
- **Method:** `GET`
- **Path:** `/products/dimensions`
- **Description:** Filter products that fit within specified maximum dimensions
- **Query Parameters:** (all optional)
  - `maxWidth` (Double): Maximum width
  - `maxHeight` (Double): Maximum height
  - `maxDepth` (Double): Maximum depth
- **Response:** List of `ProductResponseDTO` fitting the dimensional constraints

#### 11. Update Product Stock
- **Method:** `PATCH`
- **Path:** `/products/{id}/stock`
- **Description:** Updates the stock quantity for a specific product
- **Path Parameters:**
  - `id` (Long, required): Product ID (minimum value: 1)
- **Query Parameters:**
  - `quantity` (Integer, required): New stock quantity (minimum value: 0)
- **Response:** Updated `ProductResponseDTO` with HTTP 200 (OK)

#### 12. Get In-Stock Products
- **Method:** `GET`
- **Path:** `/products/in-stock`
- **Description:** Retrieves all products currently in stock
- **Response:** List of `ProductResponseDTO` for products with quantity > 0

#### 13. Get Low Stock Products
- **Method:** `GET`
- **Path:** `/products/low-stock`
- **Description:** Retrieves products with stock below a specified threshold
- **Query Parameters:**
  - `threshold` (Integer, optional): Stock threshold level (default: 10, minimum: 1)
- **Response:** List of `ProductResponseDTO` with low stock

#### 14. Get Product Filters
- **Method:** `GET`
- **Path:** `/products/filters`
- **Description:** Retrieves available filter options for product search
- **Response:** Map of filter categories to available values

**Response Format:**
```json
{
  "categories": ["Chairs", "Tables", "Sofas", "Storage"],
  "materials": ["Wood", "Metal", "Glass", "Fabric"],
  "colors": ["Black", "White", "Brown", "Gray"]
}
```

#### 15. Check Product Name Availability
- **Method:** `GET`
- **Path:** `/products/check-name`
- **Description:** Validates if a product name is unique/available
- **Query Parameters:**
  - `name` (String, required): Product name to check
  - `excludeId` (Long, optional): Product ID to exclude from uniqueness check (useful for updates)
- **Response:** JSON object indicating availability

**Response Format:**
```json
{
  "available": true
}
```

#### 16. Batch Create Products
- **Method:** `POST`
- **Path:** `/products/batch`
- **Description:** Creates multiple products in a single operation
- **Request Body:** Array of `ProductRequestDTO` objects
- **Response:** List of created `ProductResponseDTO` with HTTP 201 (Created)
- **Validation:** Each product in the array is validated using `@Valid`

---

## API Response Format

### Success Responses
- **Single Resource:** Returns the resource object directly
- **Collections:** Returns an array of resource objects
- **Confirmation Operations:** Returns a JSON object with status information

### HTTP Status Codes
- `200 OK`: Successful GET, PUT, PATCH operations
- `201 Created`: Successful POST operations
- `400 Bad Request`: Invalid request data or parameters
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Unexpected server errors

---

## Error Handling

The ProductController includes comprehensive error handling with specific exception handlers:

### 1. Not Found Error (404)
**Triggered by:** `NoSuchElementException`
**Response Format:**
```json
{
  "error": "Not Found",
  "message": "Product with ID 123 not found"
}
```

### 2. Bad Request Error (400)
**Triggered by:** `IllegalArgumentException`, validation failures
**Response Format:**
```json
{
  "error": "Bad Request",
  "message": "Invalid product data provided"
}
```

### 3. Internal Server Error (500)
**Triggered by:** Unexpected exceptions
**Response Format:**
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

---

## Data Transfer Objects

The controller uses the following DTOs for request/response handling:

- **ProductRequestDTO**: Used for creating and updating products
- **ProductResponseDTO**: Used for returning product information
  - Contains nested `DimensionsDTO` for product dimensions

---

## Validation

The controller implements comprehensive validation:

- **Request Body Validation:** Uses `@Valid` annotation on request DTOs
- **Path Parameter Validation:** Ensures IDs are positive integers (`@Min(1)`)
- **Query Parameter Validation:** Validates optional parameters with appropriate constraints
- **Controller-Level Validation:** Uses `@Validated` annotation for method-level validation

---

## Dependencies

The ProductController depends on:
- `ProductService`: Business logic layer for product operations
- Spring Boot validation framework
- Spring Web MVC framework

---

## Usage Examples

### Create a Product
```bash
POST /products
Content-Type: application/json

{
  "name": "Modern Oak Dining Table",
  "description": "Sleek dining table perfect for modern homes",
  "category": "Tables",
  "price": 599.99,
  "dimensions": {
    "width": 72.0,
    "height": 30.0,
    "depth": 36.0
  },
  "material": "Oak Wood",
  "color": "Natural",
  "stockQuantity": 15
}
```

### Search Products
```bash
GET /products/search?category=Tables&minPrice=400&maxPrice=800&inStock=true
```

### Update Stock
```bash
PATCH /products/123/stock?quantity=25
```

---

*Documentation generated for Furniture E-commerce Product Catalog Service*