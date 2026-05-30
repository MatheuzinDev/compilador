# Trabalho de Compiladores

Este projeto implementa um front-end simples de compilador usando JFlex e JCup.

## Arquivos

- `lexer.flex`: especificacao lexica usada pelo JFlex.
- `parser.cup`: gramatica sintatica e acoes semanticas usadas pelo JCup.
- `SemanticAnalyzer.java`: tabela de simbolos e checagens de tipos.
- `Main.java`: demonstra a integracao entre scanner e parser.
- `input.txt`: codigo-fonte de exemplo da linguagem.
- `inputs/`: codigos de teste separados por objetivo.
- `lib/`: arquivos `.jar` necessarios para JFlex e JCup.
- `run.sh`: gera, compila e executa a solucao integrada em Linux, macOS, WSL ou Git Bash.
- `run_all.sh`: gera, compila e executa todos os testes de `inputs/` em Linux, macOS, WSL ou Git Bash.
- `run.bat`: gera, compila e executa a solucao integrada no Windows CMD.
- `run_all.bat`: gera, compila e executa todos os testes de `inputs/` no Windows CMD.

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

Execute a partir desta pasta.

Linux, macOS, WSL ou Git Bash:

```bash
bash run.sh
```

Para executar um input especifico:

```bash
bash run.sh inputs/04_erro_sintatico.txt
```

Para executar todos os testes:

```bash
bash run_all.sh
```

Windows CMD:

```bat
run.bat
```

Para executar um input especifico:

```bat
run.bat inputs\04_erro_sintatico.txt
```

Para executar todos os testes:

```bat
run_all.bat
```

Os scripts usam os arquivos `.jar` da pasta local `lib/`:

- `jflex-full-1.9.1.jar`
- `java-cup-11b.jar`
- `java-cup-11b-runtime.jar`

## Saida Esperada

O programa imprime:

- Tokens gerados pelo JFlex, com linha e coluna.
- Resultado da analise sintatica.
- Resultado da analise semantica.

Para o `input.txt` atual, a analise sintatica e semantica deve terminar com sucesso.

## Casos de Teste

| Arquivo | Objetivo |
|---|---|
| `inputs/01_ok_completo.txt` | Programa valido completo com funcoes, variaveis, `if`, `else`, `while` e `return`. |
| `inputs/02_ok_expressoes.txt` | Programa valido focado em expressoes aritmeticas, relacionais e logicas. |
| `inputs/03_erro_lexico.txt` | Demonstra erro lexico com caractere invalido. |
| `inputs/04_erro_sintatico.txt` | Demonstra erro sintatico com `;` faltando. |
| `inputs/05_erro_variavel_nao_declarada.txt` | Usa variavel antes da declaracao. |
| `inputs/06_erro_variavel_duplicada.txt` | Declara a mesma variavel duas vezes no mesmo escopo. |
| `inputs/07_erro_tipo_atribuicao.txt` | Atribui valor boolean em variavel `int`. |
| `inputs/08_erro_funcao_nao_declarada.txt` | Chama uma funcao inexistente. |
| `inputs/09_erro_argumentos_funcao.txt` | Chama funcao com argumento de tipo incorreto. |
| `inputs/10_erro_retorno.txt` | Retorna tipo incompativel com o tipo da funcao. |
| `inputs/11_erro_condicao.txt` | Usa expressao nao booleana como condicao de `if`. |

Os testes com erro devem falhar de proposito. Eles servem para demonstrar que o analisador detecta problemas lexicos, sintaticos e semanticos.
