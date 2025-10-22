# Checkstyle - Java Linting

Checkstyle is a static code analysis tool for checking Java code against coding standards.

## Quick Start

Run Checkstyle on all sources:
```bash
./checkstyle.sh
```

## Individual Commands

```bash
# Check main sources only
./gradlew checkstyleMain

# Check test sources only
./gradlew checkstyleTest

# Check all sources
./gradlew checkstyle
```

## Reports

HTML reports are generated at:
- Main: `build/reports/checkstyle/main.html`
- Test: `build/reports/checkstyle/test.html`

XML reports (for CI/CD):
- Main: `build/reports/checkstyle/main.xml`
- Test: `build/reports/checkstyle/test.xml`

## Configuration

- **Main config**: `config/checkstyle/checkstyle.xml`
- **Suppressions**: `config/checkstyle/suppressions.xml`

### Suppressing Checks

To suppress specific checks for certain files, edit `config/checkstyle/suppressions.xml`:

```xml
<suppressions>
    <!-- Suppress all checks for generated code -->
    <suppress checks="." files="[\\/]generated[\\/]"/>
    
    <!-- Suppress specific check for specific file pattern -->
    <suppress checks="MagicNumber" files="Test\.java$"/>
</suppressions>
```

## Common Checks

- **Imports**: No star imports, no unused imports
- **Formatting**: Line length (120 chars), whitespace, newline at end of file
- **Naming**: Proper naming conventions for classes, methods, variables
- **Code Quality**: No empty blocks, simplified boolean expressions
- **Documentation**: Javadoc for public methods/classes
- **Best Practices**: Override annotations, proper exception handling

## Customization

Edit `config/checkstyle/checkstyle.xml` to:
- Enable/disable specific checks
- Adjust severity levels (error, warning, info)
- Configure thresholds (e.g., max line length, max method length)

## Integration with Build

Checkstyle runs automatically during the build process:
```bash
./gradlew build  # Includes Checkstyle checks
```

To build without Checkstyle:
```bash
./gradlew build -x checkstyleMain -x checkstyleTest
```

