#!/bin/bash
# Semgrep scan script for Product Catalog Service

set -e

echo "Running Semgrep security and code quality scan..."
echo "================================================"

# Run Semgrep with auto configuration (recommended community rules)
# and local custom rules
semgrep scan \
  --config=auto \
  --config=.semgrep.yml \
  --exclude="build/" \
  --exclude="bin/" \
  --exclude=".gradle/" \
  --verbose \
  src/

echo ""
echo "Scan complete!"
echo ""
echo "To run different Semgrep configurations:"
echo "  - Full scan:          semgrep scan --config=auto src/"
echo "  - Security only:      semgrep scan --config=p/security-audit src/"
echo "  - Java-specific:      semgrep scan --config=p/java src/"
echo "  - Spring Boot:        semgrep scan --config=p/spring src/"
echo "  - OWASP Top 10:       semgrep scan --config=p/owasp-top-ten src/"
echo "  - Custom rules only:  semgrep scan --config=.semgrep.yml src/"

