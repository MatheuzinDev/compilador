#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
LIB="$ROOT/lib"
CP=".:$LIB/java-cup-11b-runtime.jar:$LIB/java-cup-11b.jar"
INPUT="${1:-input.txt}"

cd "$ROOT"

rm -f scanner.java parser.java sym.java *.class

java -jar "$LIB/jflex-full-1.9.1.jar" lexer.flex
java -jar "$LIB/java-cup-11b.jar" -parser parser -symbols sym parser.cup
javac -cp "$CP" *.java
java -cp "$CP" Main "$INPUT"
