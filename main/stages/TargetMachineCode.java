package main.stages;

import java.util.List;

public class TargetMachineCode extends CompilerStage {

    private List<String> icr;

    public TargetMachineCode(List<String> icr) {
        this.icr = icr;
    }

    @Override
    public boolean process(String line) {
        errorMessage = null;

        System.out.println("\n======STAGE7: TARGET MACHINE CODE (TMC)\n");

        // For each ICR line, output 2 rows of 4 fixed binary values representing first letters only
        // Extract first letters of: OP, target, op1, op2 or parts of expression

        for (String code : icr) {
            // example: t1 = a * B
            String[] parts = code.split("=");
            if (parts.length < 2) continue;

            String target = parts[0].trim(); // t1
            String expr = parts[1].trim();   // a * B

            // Extract tokens from expr
            String[] tokens = expr.split("[+\\-*/]");
            // Operators split, now get operator chars
            String operators = "";
            for (char ch : expr.toCharArray()) {
                if ("+-*/".indexOf(ch) != -1) operators += ch;
            }

            // Build first letters array: [OP, target, operands...]
            // We take OP = first char of expression operator as uppercase
            // In your sample, first letters are the first letters of operands + temporaries, but here simplified:

            // For target machine code, per your example:
            // Take first letter of target, then first letters of operands and operators

            // For each operand, take first char only (upper case if letter)
            StringBuilder firstLetters = new StringBuilder();

            // Target first letter uppercase
            firstLetters.append(Character.toUpperCase(target.charAt(0)));

            // For each operand, add first char uppercase
            for (String token : tokens) {
                if (!token.isEmpty())
                    firstLetters.append(Character.toUpperCase(token.charAt(0)));
            }

            // Add operator chars (uppercase)
            for (char op : operators.toCharArray()) {
                firstLetters.append(Character.toUpperCase(op));
            }

            // We need 4 letters per row, and 2 rows
            // Pad with 't' if less than 8
            while (firstLetters.length() < 8) {
                firstLetters.append('T');
            }

            // Now print 2 rows of 4 letters in binary ASCII

            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 4; col++) {
                    char ch = firstLetters.charAt(row * 4 + col);
                    String bin = String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0');
                    System.out.print(bin + "  ");
                }
                System.out.println();
            }
            System.out.println();
        }

        System.out.println("======END OF COMPILATION");
        System.out.println("======THE ORIGINAL INPUT STRING IS: " + line.trim());

        return true;
    }
}
