#!/bin/sh

# Prefer Java 17 without requiring SDKMAN
detect_java17() {
  for CANDIDATE in \
    "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" \
    "/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" \
    "/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home" \
    "/usr/lib/jvm/java-17-openjdk-amd64" \
    "/usr/lib/jvm/java-17-openjdk"; do
    if [ -d "$CANDIDATE" ]; then
      echo "$CANDIDATE"
      return 0
    fi
  done
  return 1
}

CURRENT_JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ -z "$CURRENT_JAVA_VER" ]; then
  CURRENT_JAVA_VER=0
fi

if [ "$CURRENT_JAVA_VER" -ne 17 ]; then
  JAVA17_HOME=$(detect_java17)
  if [ -n "$JAVA17_HOME" ]; then
    export JAVA_HOME="$JAVA17_HOME"
    export PATH="$JAVA_HOME/bin:$PATH"
  fi
fi

# Delegate to the original Gradle wrapper script
. "$(dirname "$0")/gradlew.original"


