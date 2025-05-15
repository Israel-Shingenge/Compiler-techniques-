package main.stages;

public class SemanticAnalyzer extends CompilerStage {

    @Override
    public boolean process(String line) {
        errorMessage = null;

        String illegalSymbols = "%$&<>;";

        for (char c : line.toCharArray()) {
            if (illegalSymbols.indexOf(c) >= 0) {
                errorMessage = "Semantic Error: Illegal symbol '" + c + "'";
                return false;
            }
        }

        System.out.println("\n======STAGE3: COMPILER TECHNIQUES--> SEMANTIC ANALYSIS");
        System.out.println("CONCLUSION-->This expression: " + line.trim() + " is Syntactically and Semantically correct");

        return true;
    }
}
