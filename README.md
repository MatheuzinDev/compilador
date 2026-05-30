# Trabalho de Compiladores

Este projeto implementa um front-end simples de compilador usando JFlex e JCup.

## Arquivos

- `lexer.flex`: especificacao lexica usada pelo JFlex.
- `parser.cup`: gramatica sintatica e acoes semanticas usadas pelo JCup.
- `SemanticAnalyzer.java`: tabela de simbolos e checagens de tipos.
- `Main.java`: demonstra a integracao entre scanner e parser.
- `input.txt`: codigo-fonte de exemplo da linguagem.
- `run.sh`: gera, compila e executa a solucao integrada.

## Linguagem Suportada

A linguagem e parecida com Java/C, mas simplificada:

- Tipos: `int`, `float`, `boolean`, `void`.
- Funcoes com parametros.
- Declaracao de variaveis.
- Atribuicoes.
- `if`, `else`, `while`, `return`.
- Expressoes aritmeticas, relacionais e logicas.
- Comentarios de linha com `//` e bloco com `/* ... */`.

## Como Executar

Execute a partir desta pasta:

```bash
bash run.sh
```

O script usa os arquivos `.jar` da pasta `../CompiladorFrontEnd-master/lib`:

- `jflex-full-1.9.1.jar`
- `java-cup-11b.jar`
- `java-cup-11b-runtime.jar`

## Saida Esperada

O programa imprime:

- Tokens gerados pelo JFlex, com linha e coluna.
- Resultado da analise sintatica.
- Resultado da analise semantica.

Para o `input.txt` atual, a analise sintatica e semantica deve terminar com sucesso.
