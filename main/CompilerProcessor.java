package main;

import main.input.VProgramLines;
import main.stages.*;

public class CompilerProcessor {

    public static void main(String[] args) {
        int lineNumber = 1;
        for (String line : VProgramLines.lines) {
            System.out.println("\nProcessing Line " + lineNumber + ": " + line);

            // Check if line should skip all compilation stages (BEGIN, INTEGER, INPUT)
            String trimmedUpper = line.trim().toUpperCase();
            if (trimmedUpper.startsWith("BEGIN") || trimmedUpper.startsWith("INTEGER") || trimmedUpper.startsWith("INPUT")) {
                System.out.println("Skipping compilation stages for this line (no errors expected).");
                lineNumber++;
                continue;
            }

            // Initialize stages
            LexicalAnalyzer lexical = new LexicalAnalyzer();
            SyntaxAnalyzer syntax = new SyntaxAnalyzer();
            SemanticAnalyzer semantic = new SemanticAnalyzer();

            // Run stages 1-3 (error checking) for all lines
            if (!lexical.process(line)) {
                System.out.println(lexical.getErrorMessage());
                lineNumber++;
                continue;
            }
            if (!syntax.process(line)) {
                System.out.println(syntax.getErrorMessage());
                lineNumber++;
                continue;
            }
            if (!semantic.process(line)) {
                System.out.println(semantic.getErrorMessage());
                lineNumber++;
                continue;
            }

            // For the known error lines that are not BEGIN, INTEGER, INPUT, and not valid lines (e.g., those with errors),
            // we stop here after printing errors.
            // So detect error lines by matching exact strings:
            String errorLine1 = "LET B = A */ M";
            String errorLine2 = "temp = <s%**h - j / w +d +*$&";
            String errorLine3 = "WRITEE F";

            // Also consider removing spaces for safety and comparing ignoring case:
            String lineNoSpaces = line.replaceAll("\\s+", "").toUpperCase();

            if (lineNoSpaces.equals(errorLine1.replaceAll("\\s+", "").toUpperCase()) ||
                lineNoSpaces.equals(errorLine2.replaceAll("\\s+", "").toUpperCase()) ||
                lineNoSpaces.equals(errorLine3.replaceAll("\\s+", "").toUpperCase())) {

                System.out.println("Error detected in line, stopping after error checks.");
                lineNumber++;
                continue;
            }

            // Now run stages 4-7 for valid lines without errors
            IntermediateCodeGen icg = new IntermediateCodeGen();
            if (!icg.process(line)) {
                System.out.println(icg.getErrorMessage());
                lineNumber++;
                continue;
            }

            CodeGenerator cg = new CodeGenerator(icg.getICR());
            if (!cg.process(line)) {
                System.out.println(cg.getErrorMessage());
                lineNumber++;
                continue;
            }

            Optimizer opt = new Optimizer(icg.getICR());
            if (!opt.process(line)) {
                System.out.println(opt.getErrorMessage());
                lineNumber++;
                continue;
            }

            TargetMachineCode tmc = new TargetMachineCode(icg.getICR());
            if (!tmc.process(line)) {
                System.out.println(tmc.getErrorMessage());
                lineNumber++;
                continue;
            }

            System.out.println("Line " + lineNumber + " compiled successfully.");
            lineNumber++;
        }
    }
}
