package main.stages;

import java.util.List;

public class CodeGenerator extends CompilerStage {

    private List<String> icr;

    public CodeGenerator(List<String> icr) {
        this.icr = icr;
    }

    @Override
    public boolean process(String line) {
        errorMessage = null;

        System.out.println("\n======STAGE5: CODE GENERATION (CG)\n");

        // Generate simple assembly instructions from ICR lines
        for (String code : icr) {
            String[] parts = code.split("=");
            if (parts.length < 2) continue;

            String target = parts[0].trim();
            String expr = parts[1].trim();

            if (expr.contains("+")) {
                String[] ops = expr.split("\\+");
                System.out.printf("LDA %s%n", ops[0].trim());
                System.out.printf("ADD %s%n", ops[1].trim());
                System.out.printf("STR %s%n%n", target);
            } else if (expr.contains("-")) {
                String[] ops = expr.split("-");
                System.out.printf("LDA %s%n", ops[0].trim());
                System.out.printf("SUB %s%n", ops[1].trim());
                System.out.printf("STR %s%n%n", target);
            } else if (expr.contains("*")) {
                String[] ops = expr.split("\\*");
                System.out.printf("LDA %s%n", ops[0].trim());
                System.out.printf("MUL %s%n", ops[1].trim());
                System.out.printf("STR %s%n%n", target);
            } else if (expr.contains("/")) {
                String[] ops = expr.split("/");
                System.out.printf("LDA %s%n", ops[0].trim());
                System.out.printf("DIV %s%n", ops[1].trim());
                System.out.printf("STR %s%n%n", target);
            } else {
                // Assignment
                System.out.printf("LDA %s%n", expr);
                System.out.printf("STR %s%n%n", target);
            }
        }

        return true;
    }
}
