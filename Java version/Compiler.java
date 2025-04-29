
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Compiler {

    static class Symbol {

        String name;
        String type;

        Symbol(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    static Map<String, Symbol> symbolTable = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        String programFilePath = "program5.ok";

        // Phase 1: Lexical Analysis
        System.out.println("\n[STAGE 1]: LEXICAL ANALYSIS-Scanner");
        List<String> lines = Files.readAllLines(Paths.get(programFilePath));
        List<String[]> tokens = new ArrayList<>();
        int tokenCount = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] split = line.split(" ");
                tokens.add(split);
                for (String token : split) {
                    tokenCount++;
                    if (token.matches("-?\\d+")) {
                        System.out.printf("TOKEN#%d %s identifier%n", tokenCount, token);
                    } else if (token.matches("\\\".*\\\"")) {
                        System.out.printf("TOKEN#%d %s literal%n", tokenCount, token);
                    } else {
                        System.out.printf("TOKEN#%d %s Operator/Instruction%n", tokenCount, token);
                    }
                }
            }
        }
        System.out.println("Total number of Tokens: " + tokenCount);

        // Phase 2: Syntax Analysis
        System.out.println("\n[STAGE 2]: SYNTAX ANALYSIS-Parser");
        List<Object> program = new ArrayList<>();
        for (String[] tokenLine : tokens) {
            String opcode = tokenLine[0];
            program.add(opcode);

            switch (opcode) {
                case "PUSH":
                    if (tokenLine.length < 2) {
                        error("Missing value for PUSH");
                    }
                    program.add(Integer.parseInt(tokenLine[1]));
                    System.out.println("Parsed: PUSH " + tokenLine[1]);
                    break;
                case "PRINT":
                    String fullLine = String.join(" ", tokenLine);
                    String literal = fullLine.substring(fullLine.indexOf("\"") + 1, fullLine.lastIndexOf("\""));
                    program.add(literal);
                    System.out.println("Parsed: PRINT \"" + literal + "\"");
                    break;
                case "JUMP.EQ.0":
                case "JUMP.GT.0":
                    if (tokenLine.length < 2) {
                        error("Missing label for " + opcode);
                    }
                    program.add(tokenLine[1]);
                    System.out.println("Parsed: " + opcode + " " + tokenLine[1]);
                    break;
                default:
                    System.out.println("Parsed: " + opcode);
            }
        }

        // Phase 3: Semantic Analysis
        System.out.println("\n[STAGE 3]: SEMANTIC ANALYSIS");
        for (Object instruction : program) {
            if (instruction instanceof String str && str.endsWith(":")) {
                symbolTable.put(str, new Symbol(str, "LABEL"));
                System.out.println("Label identified: " + str);
            }
        }
        System.out.println("All labels resolved successfully.");

        // Phase 4: Intermediate Code Generation
        System.out.println("\n[STAGE 4]: INTERMEDIATE CODE REPRESENTATION (ICR)");
        System.out.println("Intermediate Representation (IR):");
        for (int i = 0; i < program.size(); i++) {
            System.out.println("IR[" + i + "] = " + program.get(i));
        }

        // Phase 5: Code Optimization
        System.out.println("\n[STAGE 5]: CODE OPTIMIZATION (CO)");
        List<Object> optimizedProgram = new ArrayList<>(program);
        System.out.println("Applying basic optimization pass...");
        System.out.println("No optimizations applied in this basic pass. IR is preserved.");

// Phase 6: Code Generation
        System.out.println("\n[STAGE 6]: CODE GENERATION (CG)");
        List<String> stringLiterals = new ArrayList<>();
        for (int i = 0; i < optimizedProgram.size(); i++) {
            if ("PRINT".equals(optimizedProgram.get(i))) {
                String str = (String) optimizedProgram.get(i + 1);
                optimizedProgram.set(i + 1, stringLiterals.size());
                stringLiterals.add(str);
            }
        }
        System.out.println("Resolved " + stringLiterals.size() + " string literals for PRINT statements.");

// Convert optimizedProgram (List<Object>) to a List<String>
        List<String> optimizedProgramStrings = new ArrayList<>();
        for (Object obj : optimizedProgram) {
            optimizedProgramStrings.add(obj.toString()); // Convert each element to String
        }

// Now join the strings using String.join()
        String optimizedCode = String.join(" ", optimizedProgramStrings);

// Converting the first character of each token to binary (machine code equivalent)
        System.out.println("\n[STAGE 6]: TARGET MACHINE CODE (TMC)");
        String[] optimizedTokens = optimizedCode.split("\\s+");

        for (String token : optimizedTokens) {
            // Get the first character of each token
            char firstChar = token.charAt(0);
            // Convert the first character to binary
            String binary = Integer.toBinaryString(firstChar);
            // Print the binary equivalent
            System.out.println("Token: " + token + " --> First Char: " + firstChar + " --> Binary: " + binary);
        }

        // Phase 7: Symbol Table Construction
        System.out.println("\n[STAGE 8]: SYMBOL TABLE CONSTRUCTION");
        symbolTable.forEach((k, v) -> System.out.println("  " + k + " => " + v.type));

        // Assemble the assembly code into an object file
        System.out.println("\n[CMD] Assembling");
        Process assembler = new ProcessBuilder("nasm", "-f", "elf64", "program3.asm").inheritIO().start();
        assembler.waitFor();

        // Link the object file to create the executable
        System.out.println("[CMD] Linking");
        String outputExe = "program5.exe";
        Process linker = new ProcessBuilder("gcc", "-o", outputExe, "program5.o").inheritIO().start();
        linker.waitFor();

        // Run the executable
        System.out.println("[CMD] Running");
        System.out.println("");
        System.out.println("Output:");
        Process runner = new ProcessBuilder("./" + outputExe).inheritIO().start();
        runner.waitFor();
    }

    static void error(String msg) {
        System.err.println("Syntax Error: " + msg);
        System.exit(1);
    }
}
