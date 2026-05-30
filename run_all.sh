set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
LIB="$ROOT/../CompiladorFrontEnd-master/lib"
CP=".:$LIB/java-cup-11b-runtime.jar:$LIB/java-cup-11b.jar"

cd "$ROOT"

rm -f scanner.java parser.java sym.java *.class

java -jar "$LIB/jflex-full-1.9.1.jar" lexer.flex
java -jar "$LIB/java-cup-11b.jar" -parser parser -symbols sym parser.cup
javac -cp "$CP" *.java

total=0
success=0
failed=0

for input in inputs/*.txt; do
    total=$((total + 1))
    printf 'Executando: %s\n' "$input"

    if java -cp "$CP" Main "$input"; then
        success=$((success + 1))
        printf 'Resultado do processo: SUCESSO\n'
    else
        failed=$((failed + 1))
        printf 'Resultado do processo: FALHA ESPERADA OU ERRO DETECTADO\n'
    fi
done

printf '\nResumo dos testes:\n'
printf 'Total: %d\n' "$total"
printf 'Finalizaram com sucesso: %d\n' "$success"
printf 'Detectaram erro: %d\n' "$failed"
