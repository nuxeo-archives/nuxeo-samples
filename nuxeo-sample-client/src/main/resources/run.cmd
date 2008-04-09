@echo off

rem for Java 6
set JAVA_OPTS="-Dsun.lang.ClassLoader.allowArraySyntax=true"

rem for debugging
rem set JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,address=127.0.0.1:8788,server=y,suspend=y

java -cp lib %JAVA_OPTS% Launcher lib/launcher.properties %1 %2
