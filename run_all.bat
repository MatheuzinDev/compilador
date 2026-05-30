@echo off
setlocal EnableDelayedExpansion

set "ROOT=%~dp0"
cd /d "%ROOT%"

set "LIB=%ROOT%lib"
set "CP=.;%LIB%\java-cup-11b-runtime.jar;%LIB%\java-cup-11b.jar"

del /q "scanner.java" "parser.java" "sym.java" 2>nul
del /q "*.class" 2>nul

java -jar "%LIB%\jflex-full-1.9.1.jar" lexer.flex
if errorlevel 1 exit /b 1

java -jar "%LIB%\java-cup-11b.jar" -parser parser -symbols sym parser.cup
if errorlevel 1 exit /b 1

javac -cp "%CP%" *.java
if errorlevel 1 exit /b 1

set /a total=0
set /a success=0
set /a failed=0

for %%F in (inputs\*.txt) do (
    set /a total+=1
    echo Executando: %%F

    java -cp "%CP%" Main "%%F"
    if errorlevel 1 (
        set /a failed+=1
        echo Resultado do processo: FALHA ESPERADA OU ERRO DETECTADO
    ) else (
        set /a success+=1
        echo Resultado do processo: SUCESSO
    )
)

echo.
echo Resumo dos testes:
echo Total: !total!
echo Finalizaram com sucesso: !success!
echo Detectaram erro: !failed!

exit /b 0
