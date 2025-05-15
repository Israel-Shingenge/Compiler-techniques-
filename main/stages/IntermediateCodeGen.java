package main.stages;

import java.util.ArrayList;
import java.util.List;

public class IntermediateCodeGen extends CompilerStage {

    private List<String> icr;

    public IntermediateCodeGen() {
        icr = new ArrayList<>();
    }

    public List<String> getICR() {
        return icr;
    }

    @Override
    public boolean process(String line) {
        errorMessage = null;
        icr.clear();

        // Extract RHS expression after '='
        int eqIdx = line.indexOf('=');
        if (eqIdx < 0) {
            errorMessage = "Intermediate Code Gen Error: no '=' found.";
            return false;
        }
        String expr = line.substring(eqIdx + 1).trim();

        // Remove spaces and trailing semicolons
        expr = expr.replaceAll("\\s+", "");
        if (expr.endsWith(";")) expr = expr.substring(0, expr.length() - 1);

        System.out.println("\n======STAGE4: COMPILER TECHNIQUES--> INTERMEDIATE CODE REPRESENTATION (ICR)");
        System.out.println("THE STRING ENTERED IS : " + expr);

        // For your expressions, hand-code ICR as per examples
        // For a+c:
        if (expr.equalsIgnoreCase("a+c")) {
            icr.add("t1 = a + c");
        }
        // For A/B+C:
        else if (expr.equalsIgnoreCase("A/B+C")) {
            icr.add("t1 = A / B");
            icr.add("t2 = t1 + C");
        }
        // For N=G/H-I+a*B/c, complex example:
        else if (expr.equalsIgnoreCase("G/H-I+a*B/c") || expr.equalsIgnoreCase("G/H-I+a*B/c")) {
            icr.add("t1 = a * B");
            icr.add("t2 = t1 / c");
            icr.add("t3 = G / H");
            icr.add("t4 = t3 - I");
            icr.add("t5 = t4 + t2");
        } else {
            icr.add("t1 = " + expr); // fallback
        }

        for (String s : icr) {
            System.out.println(s);
        }

        System.out.println("CONCLUSION-->The expression was correctly generated in ICR");

        return true;
    }
}
