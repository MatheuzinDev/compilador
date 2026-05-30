@echo off
setlocal

set "ROOT=%~dp0"
cd /d "%ROOT%"

set "LIB=%ROOT%lib"
set "CP=.;%LIB%\java-cup-11b-runtime.jar;%LIB%\java-cup-11b.jar"
set "INPUT=%~1"

if "%INPUT%"=="" set "INPUT=input.txt"

del /q "scanner.java" "parser.java" "sym.java" 2>nul
del /q "*.class" 2>nul

java -jar "%LIB%\jflex-full-1.9.1.jar" lexer.flex
if errorlevel 1 exit /b 1

java -jar "%LIB%\java-cup-11b.jar" -parser parser -symbols sym parser.cup
if errorlevel 1 exit /b 1

javac -cp "%CP%" *.java
if errorlevel 1 exit /b 1

java -cp "%CP%" Main "%INPUT%"
exit /b %ERRORLEVEL%
