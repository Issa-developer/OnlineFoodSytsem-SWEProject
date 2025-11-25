@echo off
setlocal
set MVNW_ROOT=%~dp0
set MAVEN_HOME=
set MAVEN_OPTS=
set JAVA_HOME=
set MAVEN_SKIP_RC=true

set WRAPPER_JAR="%MVNW_ROOT%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if not exist "%WRAPPER_JAR%" (
    echo The Maven Wrapper JAR could not be found at %WRAPPER_JAR%
    exit /b 1
)

set CMD_LINE_ARGS=
:parseArgs
if "%1"=="" goto endParseArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% "%1"
shift
goto parseArgs
:endParseArgs

set JAVA_EXE=java
if not "%JAVA_HOME%"=="" set JAVA_EXE=%JAVA_HOME%\bin\java

"%JAVA_EXE%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" %WRAPPER_LAUNCHER% %CMD_LINE_ARGS%
endlocal
