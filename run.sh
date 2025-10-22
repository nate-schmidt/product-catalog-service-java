#!/bin/bash
# Convenience script to run the application with the correct Java environment

# Set up Java 17 environment
source ./setenv.sh

if [ $? -ne 0 ]; then
    echo "❌ Failed to set up Java environment"
    exit 1
fi

echo ""
echo "🚀 Starting Product Catalog Service..."
echo "📍 API will be available at: http://localhost:8080/api"
echo ""

# Run the application
./gradlew bootRun

