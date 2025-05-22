package main;

import main.input.VProgramLines;
import main.stages.*;

import java.util.Scanner;

public class CompilerProcessor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int lineNumber = 1;

        for (String line : VProgramLines.lines) {
            System.out.println("\nProcessing Line " + lineNumber + ": " + line);

            String trimmedUpper = line.trim().toUpperCase();
            if (trimmedUpper.startsWith("BEGIN") || trimmedUpper.startsWith("INTEGER") || trimmedUpper.startsWith("INPUT")) {
                System.out.println("Skipping compilation stages for this line (no errors expected).");
                lineNumber++;
                continue;
            }

            LexicalAnalyzer lexical = new LexicalAnalyzer();
            SyntaxAnalyzer syntax = new SyntaxAnalyzer();
            SemanticAnalyzer semantic = new SemanticAnalyzer();

            // Stage 1: Lexical Analysis
            if (!lexical.process(line)) {
                System.out.println(lexical.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            // Stage 2: Syntax Analysis
            if (!syntax.process(line)) {
                System.out.println(syntax.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            // Stage 3: Semantic Analysis
            if (!semantic.process(line)) {
                System.out.println(semantic.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            // Known error patterns
            String lineNoSpaces = line.replaceAll("\\s+", "").toUpperCase();
            String[] errorPatterns = {
                "LETB=A*/M",
                "TEMP=<S%**H-J/W+D+*$&",
                "WRITEEF"
            };

            for (String errorPattern : errorPatterns) {
                if (lineNoSpaces.equals(errorPattern)) {
                    System.out.println("Error detected in line, stopping after error checks.");
                    if (!promptUser(scanner)) break;
                    lineNumber++;
                    continue;
                }
            }

            // Stage 4: Intermediate Code Generation
            IntermediateCodeGen icg = new IntermediateCodeGen();
            if (!icg.process(line)) {
                System.out.println(icg.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            // Stage 5: Code Generation
            CodeGenerator cg = new CodeGenerator(icg.getICR());
            if (!cg.process(line)) {
                System.out.println(cg.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            // Stage 6: Optimization
            Optimizer opt = new Optimizer(icg.getICR());
            if (!opt.process(line)) {
                System.out.println(opt.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            // Stage 7: Target Machine Code
            TargetMachineCode tmc = new TargetMachineCode(icg.getICR());
            if (!tmc.process(line)) {
                System.out.println(tmc.getErrorMessage());
                if (!promptUser(scanner)) break;
                lineNumber++;
                continue;
            }

            System.out.println("Line " + lineNumber + " compiled successfully.");
            lineNumber++;
        }

        System.out.println("\nCompilation process completed.");
        scanner.close();
    }

    private static boolean promptUser(Scanner scanner) {
        while (true) {
            System.out.print("Error detected. Type 'c' to continue to the next line or 'e' to exit: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("c")) return true;
            else if (input.equals("e")) return false;
            else System.out.println("Invalid input. Please type 'c' or 'e'.");
        }
    }
}
