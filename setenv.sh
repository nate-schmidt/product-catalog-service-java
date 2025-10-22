#!/bin/bash
# Environment setup script for Java 17
# This script sets up the correct Java version for the project

# Detect the operating system
OS="$(uname -s)"

case "${OS}" in
    Darwin*)
        # macOS - using Homebrew paths
        if [ -d "/opt/homebrew/opt/openjdk@17" ]; then
            export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
            export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
            echo "✓ Java 17 environment configured (Homebrew)"
        elif [ -d "/usr/local/opt/openjdk@17" ]; then
            export JAVA_HOME="/usr/local/opt/openjdk@17"
            export PATH="/usr/local/opt/openjdk@17/bin:$PATH"
            echo "✓ Java 17 environment configured (Homebrew - Intel)"
        elif [ -d "/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home" ]; then
            export JAVA_HOME="/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home"
            export PATH="$JAVA_HOME/bin:$PATH"
            echo "✓ Java 17 environment configured (System)"
        else
            echo "⚠ Java 17 not found. Please install it with: brew install openjdk@17"
            exit 1
        fi
        ;;
    Linux*)
        # Linux - common paths
        if [ -d "/usr/lib/jvm/java-17-openjdk" ]; then
            export JAVA_HOME="/usr/lib/jvm/java-17-openjdk"
            export PATH="$JAVA_HOME/bin:$PATH"
            echo "✓ Java 17 environment configured (Linux)"
        elif [ -d "/usr/lib/jvm/java-17-openjdk-amd64" ]; then
            export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
            export PATH="$JAVA_HOME/bin:$PATH"
            echo "✓ Java 17 environment configured (Linux)"
        else
            echo "⚠ Java 17 not found. Please install it with your package manager"
            exit 1
        fi
        ;;
    *)
        echo "⚠ Unsupported operating system: ${OS}"
        exit 1
        ;;
esac

# Verify Java version
java -version 2>&1 | head -n 1

