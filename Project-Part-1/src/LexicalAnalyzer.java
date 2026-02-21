import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private static final List<String> KEYWORDS = List.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for",
            "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "_");
    /* Character classes */
    private final static int LETTER = 0;
    private final static int DIGIT = 1;
    private final static int UNKNOWN = 99;
    /* Token codes */
    private final static int INT_LIT = 10;
    private final static int DOUBLE_LIT = 11;
    private final static int FLOAT_LIT = 12;
    private final static int CHAR_LIT = 13;
    private final static int STR_LIT = 14;
    private final static int IDENT = 15;

    private final static int ASSIGN_OP = 20;
    private final static int ADD_OP = 21;
    private final static int SUB_OP = 22;
    private final static int MULT_OP = 23;
    private final static int DIV_OP = 24;

    private final static int LEFT_PAREN = 30;
    private final static int RIGHT_PAREN = 31;
    private final static int LEFT_C_BRACE = 32;
    private final static int RIGHT_C_BRACE = 33;
    private final static int LEFT_BRACK = 34;
    private final static int RIGHT_BRACK = 35;

    private static char nextChar;
    private static String lexeme;
    private static String line;
    private static String tokenClass;

    public static LexerResults analyze(String fileName) {
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            ArrayList<String> identifiers = new ArrayList<>();
            ArrayList<String> tokens = new ArrayList<>();
            ArrayList<String> lexemes = new ArrayList<>();

            while((line = file.readLine()) != null) {

            }

            tokens.add("-1");
            lexemes.add("EOF");

            return new LexerResults(identifiers, tokens, lexemes);
        } catch(IOException e) {
            System.out.println("File not found!");
            return null;
        }
    }

    private void getChar() {
        skipBlank();
        int index = 0;
        if(line.isEmpty()) {
            nextChar = '\0';
        }
        if(line.charAt(0) == '"') {
            tokenClass
        }
    }

    private void addChar() {
        lexeme += nextChar;
        nextChar = '\0';
    }

    private void skipBlank() {
        line = "";
        lexeme = "";
        int index = 0;
        while(line.charAt(index) == ' ') {
            index++;
        }
        line = line.substring(index);
    }

    private String getTokenClass(char firstChar) {
        firstChar = (firstChar + "").toLowerCase().charAt(0);
        switch(firstChar) {
            case '(':


        }
    }

    private boolean isDigit(char input) {
        try {
            Integer.parseInt(input + "");
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private boolean isLetter(char input) {
        String temp = (input + "").toLowerCase();
        return "abcdefghijklmnopqrstuvwxyz".contains(temp);
    }
}
