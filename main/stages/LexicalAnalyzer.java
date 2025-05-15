package main.stages;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer extends CompilerStage {

    private List<Token> tokens;

    public LexicalAnalyzer() {
        tokens = new ArrayList<>();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public boolean process(String line) {
        errorMessage = null;
        tokens.clear();

        System.out.println("======STAGE1: COMPILER TECHNIQUES--> LEXICAL ANALYSIS-Scanner");
        System.out.println("SYMBOL TABLE COMPRISING ATTRIBUTES AND TOKENS:\n");

        String tempLine = line.replaceAll("([=+\\-*/;])", " $1 ");
        String[] parts = tempLine.trim().split("\\s+");

        int tokenCount = 0;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) continue;
            tokenCount++;

            String tokenType;
            if (part.matches("[A-Za-z]+")) {
                if (isKeyword(part)) {
                    if (!isValidKeyword(part)) {
                        System.out.println("TOKEN#" + tokenCount + " " + part + " identifier");
                        System.out.println("Total number of Tokens: " + tokenCount);
                        System.out.println("GIVEN THE GRAMMAR: E=E1 | E=E1*E2 | E=E1+E2 | E=digit | E={0,1,2,3,4,5,6,7,8,9}");
                        System.out.println("\nERROR!\nLexical Error: Misspelling in the keywords such as: " + part + " not allowed");
                        return false;
                    }
                    tokenType = "keyword";
                } else {
                    tokenType = "identifier";
                }
            } else if (part.matches("[=;+\\-*/]")) {
                if ("=;".contains(part)) tokenType = "symbol";
                else tokenType = "operator";
            } else {
                errorMessage = "Lexical Error: Invalid token '" + part + "'";
                return false;
            }

            tokens.add(new Token(tokenCount, part, tokenType));
            System.out.printf("TOKEN#%d %s %s%n", tokenCount, part, tokenType);

            // Check for combined operators like */ or *+
            if (tokenType.equals("operator") && i + 1 < parts.length) {
                String nextPart = parts[i + 1];
                if (nextPart.matches("[+\\-*/]")) {
                    System.out.println("Total number of Tokens: " + (tokenCount + 1));
                    System.out.println("GIVEN THE GRAMMAR: E=E1 | E=E1*E2 | E=E1+E2 | E=digit | E={0,1,2,3,4,5,6,7,8,9}");
                    System.out.println("\nERROR!\nSyntax Error: Combined operators '" + part + nextPart + "' are not allowed.");
                    System.out.println("Syntax Error: Two combined operators found (e.g., +*, -/, */, *+).");
                    return false;
                }
            }
        }

        System.out.println("Total number of Tokens: " + tokens.size());
        System.out.println("GIVEN THE GRAMMAR: E=E1 | E=E1*E2 | E=E1+E2 | E=digit | E={0,1,2,3,4,5,6,7,8,9}");

        return true;
    }

    private boolean isKeyword(String word) {
        // Accepts both valid and invalid keyword lookalikes
        String[] keywords = {"BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END", "WRITEE"};
        for (String kw : keywords) {
            if (kw.equalsIgnoreCase(word)) return true;
        }
        return false;
    }

    private boolean isValidKeyword(String word) {
        // Only accepts strictly valid keywords
        String[] validKeywords = {"BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"};
        for (String kw : validKeywords) {
            if (kw.equalsIgnoreCase(word)) return true;
        }
        return false;
    }

    public static class Token {
        private int number;
        private String value;
        private String type;

        public Token(int number, String value, String type) {
            this.number = number;
            this.value = value;
            this.type = type;
        }

        public int getNumber() { return number; }
        public String getValue() { return value; }
        public String getType() { return type; }
    }
}
