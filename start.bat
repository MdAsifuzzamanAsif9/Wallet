@echo off
setlocal

cd /d "%~dp0"

if /I "%~1"=="--help" goto :help

where java >nul 2>nul
if errorlevel 1 (
    echo Java runtime was not found in PATH.
    echo Please install a JDK or JRE and try again.
    pause
    exit /b 1
)

where javac >nul 2>nul
if errorlevel 1 (
    echo Java compiler was not found in PATH.
    echo Please install a JDK and try again.
    pause
    exit /b 1
)

if not exist "outclasses" mkdir "outclasses"

echo Compiling Wallet...
javac -d outclasses src\wallettrial_2\*.java
if errorlevel 1 (
    echo.
    echo Compilation failed. Please review the errors above.
    pause
    exit /b 1
)

echo Starting Wallet...
java -cp outclasses wallettrial_2.App
if errorlevel 1 (
    echo.
    echo The application closed with an error.
    pause
    exit /b 1
)

exit /b 0

:help
echo Wallet launcher
echo.
echo Double-click this file to compile and start the app.
echo Requirements:
echo   - Java JDK installed
echo   - java and javac available in PATH
echo.
pause
exit /b 0
