# Development Environment Setup

This document explains how the Java 17 environment is configured for this project.

## Overview

This project requires **Java 17** to run properly. We've set up multiple ways to ensure the correct Java version is used.

## Configuration Files

### 1. `build.gradle`
The build file includes Java Toolchain configuration:
```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```
This tells Gradle to use Java 17 for compilation and runtime, automatically downloading it if necessary.

### 2. `gradle.properties`
Contains Gradle performance optimizations and placeholder comments for Java home configuration:
- `org.gradle.jvmargs`: JVM memory settings
- `org.gradle.parallel`: Enable parallel builds
- `org.gradle.caching`: Enable build caching

### 3. `setenv.sh`
A shell script that automatically detects and sets up Java 17 on your system:
- Detects your OS (macOS/Linux)
- Finds Java 17 installation
- Sets `JAVA_HOME` and `PATH` variables
- Works with Homebrew, system installs, and common package managers

### 4. `run.sh`
A convenience wrapper script that:
1. Sources `setenv.sh` to set up the environment
2. Runs `./gradlew bootRun` to start the application

## Usage

### Easiest Method (Recommended)
```bash
./run.sh
```

### Manual Method
```bash
source setenv.sh
./gradlew bootRun
```

### If Java 17 is Your Default
```bash
./gradlew bootRun
```

## Installing Java 17

If you don't have Java 17 installed:

### macOS (Homebrew)
```bash
brew install openjdk@17
```

### Ubuntu/Debian
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

### RHEL/CentOS/Fedora
```bash
sudo dnf install java-17-openjdk-devel
```

## How It Works

1. **Java Toolchain**: Gradle's Java Toolchain feature automatically detects or downloads Java 17
2. **Environment Scripts**: The `setenv.sh` script configures your shell environment
3. **Gradle Daemon**: Gradle caches the Java configuration in its daemon for faster subsequent builds

## Troubleshooting

### "Unsupported class file major version" error
This means Gradle is using the wrong Java version. Solution:
```bash
./gradlew --stop  # Stop all Gradle daemons
source setenv.sh   # Set up environment
./gradlew bootRun  # Restart
```

### "Java 17 not found" error
Install Java 17 using the instructions above, then run `./run.sh` again.

### Checking your Java version
```bash
java -version
```
Should show: `openjdk version "17.0.x"`

## Additional Notes

- All build artifacts are in `build/` and `.gradle/` directories (gitignored)
- Lombok 1.18.30 is used for compatibility with Java 17
- The project uses Spring Boot 3.2.0 which requires Java 17+

