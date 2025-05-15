package main.stages;

import java.util.List;

public class Optimizer extends CompilerStage {

    private List<String> icr;

    public Optimizer(List<String> icr) {
        this.icr = icr;
    }

    @Override
    public boolean process(String line) {
        errorMessage = null;

        System.out.println("\n======STAGE6: CODE OPTIMISATION (CO)\n");

        // Print optimized instructions in format: OP target, op1, op2
        for (String code : icr) {
            String[] parts = code.split("=");
            if (parts.length < 2) continue;

            String target = parts[0].trim();
            String expr = parts[1].trim();

            if (expr.contains("+")) {
                String[] ops = expr.split("\\+");
                System.out.printf("ADD %s, %s, %s%n%n", target, ops[0].trim(), ops[1].trim());
            } else if (expr.contains("-")) {
                String[] ops = expr.split("-");
                System.out.printf("SUB %s, %s, %s%n%n", target, ops[0].trim(), ops[1].trim());
            } else if (expr.contains("*")) {
                String[] ops = expr.split("\\*");
                System.out.printf("MUL %s, %s, %s%n%n", target, ops[0].trim(), ops[1].trim());
            } else if (expr.contains("/")) {
                String[] ops = expr.split("/");
                System.out.printf("DIV %s, %s, %s%n%n", target, ops[0].trim(), ops[1].trim());
            } else {
                // Assignment fallback
                System.out.printf("MOV %s, %s%n%n", target, expr);
            }
        }

        return true;
    }
}
