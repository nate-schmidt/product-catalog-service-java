# Cursor Hooks - Auto-formatter

This project is configured with Cursor hooks that automatically format Java code after each edit.

## Configuration

**Location**: `.cursor/hooks/hooks.json`

```json
{
  "version": 1,
  "hooks": {
    "afterFileEdit": [
      {
        "command": "./.cursor/hooks/format-java.sh"
      }
    ]
  }
}
```

## What it Does

The `afterFileEdit` hook runs automatically after you edit any file and performs:

1. **Google Java Format** - Applies Google's Java style guide formatting
2. **Add Newlines** - Ensures files end with a newline (Checkstyle requirement)
3. **Organize Imports** - Removes duplicate imports

## Components

### 1. Hook Configuration
- File: `.cursor/hooks/hooks.json`
- Triggers after every file edit
- Runs the formatting script

### 2. Formatting Script
- File: `.cursor/hooks/format-java.sh`
- Executable shell script
- Only processes `.java` files
- Skips files in `build/` or `generated/` directories

### 3. Google Java Format
- File: `google-java-format.jar` (in project root)
- Version: 1.19.2
- Automatically formats Java code to match style guide
- Fixes most Checkstyle violations automatically

## What Gets Auto-Fixed

✅ **Automatically Fixed:**
- Indentation and whitespace
- Line wrapping and formatting
- Missing newlines at end of files
- Import organization
- Bracket placement
- Most Checkstyle style violations

❌ **Not Automatically Fixed:**
- Missing Javadoc comments
- Unused imports (partially)
- Logic errors or code smells
- Some Checkstyle violations (e.g., method length)

## How to Test

Edit any Java file and save it. The hook will automatically format the file.

You can also manually run the formatter:
```bash
java -jar google-java-format.jar --replace src/main/java/**/*.java
```

## Disabling the Hook

To temporarily disable the hook, rename or move the hooks configuration:
```bash
mv .cursor/hooks/hooks.json .cursor/hooks/hooks.json.disabled
```

To re-enable:
```bash
mv .cursor/hooks/hooks.json.disabled .cursor/hooks/hooks.json
```

## References

- [Cursor Hooks Documentation](https://cursor.com/docs/agent/hooks)
- [Google Java Format](https://github.com/google/google-java-format)
- [Checkstyle Documentation](https://checkstyle.org/)

