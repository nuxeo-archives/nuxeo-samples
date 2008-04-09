#!/bin/sh

# for Java 6
JAVA_OPTS="-Dsun.lang.ClassLoader.allowArraySyntax=true"

# for debugging
#JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=127.0.0.1:8788,server=y,suspend=y"

java -cp lib $JAVA_OPTS Launcher lib/launcher.properties "$@"
