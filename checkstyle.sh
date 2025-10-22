#!/bin/bash

# Checkstyle runner script for product-catalog-service
# This script runs Checkstyle on the Java codebase

set -e

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Running Checkstyle on Java code...${NC}"
echo ""

# Run Checkstyle on main sources
echo "Checking main sources..."
./gradlew checkstyleMain

# Run Checkstyle on test sources
echo "Checking test sources..."
./gradlew checkstyleTest

echo ""
echo -e "${GREEN}✓ Checkstyle checks passed!${NC}"
echo ""
echo "Reports available at:"
echo "  - Main: build/reports/checkstyle/main.html"
echo "  - Test: build/reports/checkstyle/test.html"

