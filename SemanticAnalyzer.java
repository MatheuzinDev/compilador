import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {
    private static final String ERROR_TYPE = "error";

    private static class FunctionInfo {
        final String name;
        final String returnType;
        final List<String> parameters = new ArrayList<>();
        boolean hasReturn;

        FunctionInfo(String name, String returnType) {
            this.name = name;
            this.returnType = returnType;
        }
    }

    private final Map<String, FunctionInfo> functions = new LinkedHashMap<>();
    private final Deque<Map<String, String>> scopes = new ArrayDeque<>();
    private final List<String> errors = new ArrayList<>();
    private FunctionInfo currentFunction;

    public void beginFunction(String returnType, String name) {
        FunctionInfo function = new FunctionInfo(name, returnType);

        if (functions.containsKey(name)) {
            error("Funcao '" + name + "' ja foi declarada.");
        } else {
            functions.put(name, function);
        }

        currentFunction = function;
        enterScope();
    }

    public void endFunction() {
        if (currentFunction != null && !"void".equals(currentFunction.returnType) && !currentFunction.hasReturn) {
            error("Funcao '" + currentFunction.name + "' precisa retornar '" + currentFunction.returnType + "'.");
        }

        exitScope();
        currentFunction = null;
    }

    public void addParameter(String type) {
        if (currentFunction != null) {
            currentFunction.parameters.add(type);
        }
    }

    public void enterScope() {
        scopes.push(new LinkedHashMap<>());
    }

    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    public void declareVariable(String type, String name) {
        if (scopes.isEmpty()) {
            enterScope();
        }

        Map<String, String> scope = scopes.peek();
        if (scope.containsKey(name)) {
            error("Variavel '" + name + "' ja foi declarada neste escopo.");
            return;
        }

        scope.put(name, type);
    }

    public String useVariable(String name) {
        String type = findVariable(name);
        if (type == null) {
            error("Variavel '" + name + "' usada antes da declaracao.");
            return ERROR_TYPE;
        }

        return type;
    }

    public void assignVariable(String name, String expressionType) {
        String targetType = findVariable(name);
        if (targetType == null) {
            error("Variavel '" + name + "' recebeu valor antes da declaracao.");
            return;
        }

        checkAssignment(targetType, expressionType, "atribuicao para '" + name + "'");
    }

    public void checkAssignment(String targetType, String expressionType, String context) {
        if (!isCompatible(targetType, expressionType)) {
            error("Tipo invalido em " + context + ": esperado '" + targetType + "', encontrado '" + expressionType + "'.");
        }
    }

    public void checkReturn(String expressionType) {
        if (currentFunction == null) {
            error("Return fora de funcao.");
            return;
        }

        currentFunction.hasReturn = true;

        if ("void".equals(currentFunction.returnType)) {
            if (!"void".equals(expressionType)) {
                error("Funcao '" + currentFunction.name + "' nao deve retornar valor.");
            }
            return;
        }

        if ("void".equals(expressionType)) {
            error("Funcao '" + currentFunction.name + "' deve retornar '" + currentFunction.returnType + "'.");
            return;
        }

        checkAssignment(currentFunction.returnType, expressionType, "retorno da funcao '" + currentFunction.name + "'");
    }

    public String callFunction(String name, List args) {
        FunctionInfo function = functions.get(name);
        if (function == null) {
            error("Funcao '" + name + "' nao foi declarada.");
            return ERROR_TYPE;
        }

        int expected = function.parameters.size();
        int received = args == null ? 0 : args.size();
        if (expected != received) {
            error("Funcao '" + name + "' espera " + expected + " argumento(s), mas recebeu " + received + ".");
            return function.returnType;
        }

        for (int i = 0; i < expected; i++) {
            String expectedType = function.parameters.get(i);
            String receivedType = (String) args.get(i);
            if (!isCompatible(expectedType, receivedType)) {
                error("Argumento " + (i + 1) + " da funcao '" + name + "' deve ser '" + expectedType + "', encontrado '" + receivedType + "'.");
            }
        }

        return function.returnType;
    }

    public void requireBoolean(String type, String context) {
        if (!ERROR_TYPE.equals(type) && !"boolean".equals(type)) {
            error("A " + context + " deve ser boolean, encontrado '" + type + "'.");
        }
    }

    public String checkLogical(String left, String right) {
        if (ERROR_TYPE.equals(left) || ERROR_TYPE.equals(right)) {
            return ERROR_TYPE;
        }

        if (!"boolean".equals(left) || !"boolean".equals(right)) {
            error("Operacao logica requer operandos boolean.");
            return ERROR_TYPE;
        }

        return "boolean";
    }

    public String checkEquality(String left, String right) {
        if (ERROR_TYPE.equals(left) || ERROR_TYPE.equals(right)) {
            return ERROR_TYPE;
        }

        if (isCompatible(left, right) || isCompatible(right, left)) {
            return "boolean";
        }

        error("Comparacao de igualdade entre tipos incompativeis: '" + left + "' e '" + right + "'.");
        return ERROR_TYPE;
    }

    public String checkRelational(String left, String right) {
        if (ERROR_TYPE.equals(left) || ERROR_TYPE.equals(right)) {
            return ERROR_TYPE;
        }

        if (!isNumeric(left) || !isNumeric(right)) {
            error("Comparacao relacional requer operandos numericos.");
            return ERROR_TYPE;
        }

        return "boolean";
    }

    public String checkArithmetic(String left, String right) {
        if (ERROR_TYPE.equals(left) || ERROR_TYPE.equals(right)) {
            return ERROR_TYPE;
        }

        if (!isNumeric(left) || !isNumeric(right)) {
            error("Operacao aritmetica requer operandos numericos.");
            return ERROR_TYPE;
        }

        if ("float".equals(left) || "float".equals(right)) {
            return "float";
        }

        return "int";
    }

    public String checkUnaryMinus(String type) {
        if (ERROR_TYPE.equals(type)) {
            return ERROR_TYPE;
        }

        if (!isNumeric(type)) {
            error("Operador '-' unario requer operando numerico.");
            return ERROR_TYPE;
        }

        return type;
    }

    public String checkNot(String type) {
        if (ERROR_TYPE.equals(type)) {
            return ERROR_TYPE;
        }

        if (!"boolean".equals(type)) {
            error("Operador '!' requer operando boolean.");
            return ERROR_TYPE;
        }

        return "boolean";
    }

    public void validateProgram() {
        FunctionInfo main = functions.get("main");
        if (main == null) {
            error("Funcao 'main' nao foi declarada.");
            return;
        }

        if (!main.parameters.isEmpty()) {
            error("Funcao 'main' nao deve receber parametros.");
        }

        if (!"int".equals(main.returnType) && !"void".equals(main.returnType)) {
            error("Funcao 'main' deve retornar int ou void.");
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void printReport() {
        if (errors.isEmpty()) {
            System.out.println("Analise semantica: OK");
            return;
        }

        System.out.println("Analise semantica encontrou " + errors.size() + " erro(s):");
        for (String error : errors) {
            System.out.println("- " + error);
        }
    }

    private String findVariable(String name) {
        for (Map<String, String> scope : scopes) {
            String type = scope.get(name);
            if (type != null) {
                return type;
            }
        }

        return null;
    }

    private boolean isCompatible(String targetType, String expressionType) {
        if (ERROR_TYPE.equals(targetType) || ERROR_TYPE.equals(expressionType)) {
            return true;
        }

        if (targetType.equals(expressionType)) {
            return true;
        }

        return "float".equals(targetType) && "int".equals(expressionType);
    }

    private boolean isNumeric(String type) {
        return "int".equals(type) || "float".equals(type);
    }

    private void error(String message) {
        errors.add(message);
    }
}
