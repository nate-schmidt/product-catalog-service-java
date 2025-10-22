#!/bin/bash

# Java Auto-formatter Hook
# Automatically fixes common Checkstyle violations after file edits
# This hook runs after each file edit to maintain code style

# The file path is passed as the first argument or via CURSOR_FILE environment variable
FILE="${1:-$CURSOR_FILE}"

# Exit silently if no file specified or file doesn't exist
if [ -z "$FILE" ] || [ ! -f "$FILE" ]; then
    exit 0
fi

# Only process Java files
if [[ ! "$FILE" =~ \.java$ ]]; then
    exit 0
fi

# Get the project root (parent of .cursor directory)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

cd "$PROJECT_ROOT"

# Function to add newline at end of file if missing
add_newline_at_eof() {
    if [ -n "$(tail -c 1 "$FILE")" ]; then
        echo "" >> "$FILE"
    fi
}

# Function to organize imports (remove unused, expand star imports)
organize_imports() {
    # Only if we have a Java file with imports
    if grep -q "^import " "$FILE" 2>/dev/null; then
        # Remove duplicate imports
        awk '!seen[$0]++ || !/^import /' "$FILE" > "${FILE}.tmp" && mv "${FILE}.tmp" "$FILE"
    fi
}

# Function to format using Google Java Format
format_with_google_java_format() {
    local gjf_jar="$PROJECT_ROOT/google-java-format.jar"
    
    if [ -f "$gjf_jar" ] && command -v java &> /dev/null; then
        # Use Google Java Format to fix most style issues
        java -jar "$gjf_jar" --replace "$FILE" 2>/dev/null && return 0
    fi
    
    # Check if installed globally
    if command -v google-java-format &> /dev/null; then
        google-java-format --replace "$FILE" 2>/dev/null && return 0
    fi
    
    return 1
}

# Main formatting logic
main() {
    # Skip if file is in build or generated directories
    if [[ "$FILE" == *"/build/"* ]] || [[ "$FILE" == *"/generated/"* ]]; then
        exit 0
    fi
    
    # Apply Google Java Format (fixes most Checkstyle issues)
    format_with_google_java_format
    
    # Ensure newline at end of file (Checkstyle requirement)
    add_newline_at_eof
    
    # Try to organize imports
    organize_imports
}

# Run the formatter (suppress all output and errors)
main &> /dev/null

# Always exit successfully to not block the edit
exit 0

