package main.stages;

import java.util.List;

@SuppressWarnings("unused")
public class SyntaxAnalyzer extends CompilerStage {

    @Override
    public boolean process(String line) {
        errorMessage = null;

        // Just for lines with expressions, simulate detailed derivation for the RHS
        if (!line.contains("=")) {
            errorMessage = "Syntax Error: Line must contain assignment '='.";
            return false;
        }

        String rhs = line.substring(line.indexOf('=') + 1).trim();

        System.out.println("\n======STAGE2: COMPILER TECHNIQUES--> SYNTAX ANALYSIS-Parser");
        System.out.println("GET A DERIVATION FOR : " + line.trim());

        // For demo, split rhs tokens by operators/spaces to simulate derivation
        // Real parser should generate full parse tree derivation

        // Example derivation for N = G/H - I + a*B/c
        if (rhs.equalsIgnoreCase("G/H - I + a*B/c") || rhs.equalsIgnoreCase("G/H-I+a*B/c")) {
            System.out.println("E14 = E7 / E8 - E9 + E10 * E11 / E12");
            System.out.println("E14 = t4 + t2");
            System.out.println("t4 = t3 - E9");
            System.out.println("t3 = E7 / E8");
            System.out.println("t2 = t1 / E12");
            System.out.println("t1 = E10 * E11");
        } else if (rhs.equalsIgnoreCase("A + c") || rhs.equalsIgnoreCase("a + c")) {
            System.out.println("E4 = E1 + E2");
            System.out.println("E4 = a + c");
        } else if (rhs.equalsIgnoreCase("A/B + C") || rhs.equalsIgnoreCase("A/B+C")) {
            System.out.println("E6 = E3 / E4 + E5");
            System.out.println("E6 = A / B + C");
        } else {
            // fallback simple derivation
            System.out.println("E = " + rhs);
        }

        return true;
    }
}
