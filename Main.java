import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) {
        String input = args.length > 0 ? args[0] : "input.txt";

        try {
            System.out.println("Arquivo de entrada: " + input);
            printTokens(input);
            runParser(input);
        } catch (Exception exception) {
            System.err.println("Falha ao processar o arquivo: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static void printTokens(String input) throws Exception {
        System.out.println();
        System.out.println("Tokens gerados pelo JFlex:");

        try (Reader reader = Files.newBufferedReader(Path.of(input))) {
            scanner lexer = new scanner(reader);
            Symbol token;

            while ((token = lexer.next_token()).sym != sym.EOF) {
                String tokenName = sym.terminalNames[token.sym];
                String value = token.value == null ? "" : " -> " + token.value;
                System.out.printf("%-15s linha %-3d coluna %-3d%s%n", tokenName, token.left, token.right, value);
            }
        }
    }

    private static void runParser(String input) throws Exception {
        System.out.println();
        System.out.println("Analise sintatica e semantica com JCup:");

        parser parser;
        try (Reader reader = Files.newBufferedReader(Path.of(input))) {
            scanner lexer = new scanner(reader);
            parser = new parser(lexer);
            parser.parse();
        }

        System.out.println("Analise sintatica: OK");
        parser.sem().printReport();

        if (parser.sem().hasErrors()) {
            System.exit(1);
        }
    }
}
