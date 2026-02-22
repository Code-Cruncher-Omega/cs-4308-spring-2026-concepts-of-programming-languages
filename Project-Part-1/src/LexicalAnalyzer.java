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
    private static ArrayList<String> identifiers;   // Not used, but stored in case of future use
    private static ArrayList<String> tokens;    // Same size as lexemes arraylist
    private static ArrayList<String> lexemes;

    private static String lexeme = "";
    private static String line;
    private static String tokenClass;

    // ONLY WORKS IF THE FILE IS SYNTACTICALLY CORRECT IN THE JAVA PROGRAMMING LANGUAGE!!!!!
    public static LexerResults printAnalysis(String fileName) {
        LexerResults results = analyze(fileName);
        if(results == null) {
            return null;
        }
        System.out.println("Token\t\t\t\tLexeme");
        System.out.println();
        for(int i = 0 ; i < tokens.size() ; i++) {
            String tempToken = tokens.get(i);
            String tempLexeme = lexemes.get(i);
            System.out.println(tempToken + ((tempToken.length() < 8) ? "\t\t\t\t" : "\t\t\t") + tempLexeme);
        }
        System.out.println();
        System.out.println("Total pairs of tokens and lexemes: " + tokens.size() + " (Including 'EOF' and 'END OF FILE')");
        return results;
    }

    public static LexerResults analyze(String fileName) {
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            identifiers = new ArrayList<>();
            tokens = new ArrayList<>();
            lexemes = new ArrayList<>();
            while((line = file.readLine()) != null) {
                analyzeLine();
            }
            tokens.add("EOF");
            lexemes.add("END OF FILE");
            return new LexerResults(identifiers, tokens, lexemes);
        } catch(IOException e) {
            System.out.println("File not found!");
            return null;
        }
    }

    private static void analyzeLine() {
        while(!line.isEmpty()) {
            nextLexeme();
            buildLexeme();
            if(lexeme.isEmpty() || tokenClass.isEmpty()) {
                continue;
            }
            tokens.add(tokenClass);
            lexemes.add(lexeme);
        }
    }

    private static void nextLexeme() {
        int index = 0;
        while(line.charAt(index) == ' ') {
            index++;
        }
        line = line.substring(index);
    }

    private static void buildLexeme() {
        lexeme = "";
        tokenClass = "";
        if(line.isEmpty()) {
            return;
        }
        int index = 1;
        char firstChar = line.charAt(0);
        tokenClass = getTokenClass(firstChar);
        if(tokenClass.equals("UNKNOWN")) {
            if(firstChar == '"') {
                tokenClass = "STR_LITERAL";
                lexeme += "\"";
                char nextChar;
                int increment;
                while(!line.isEmpty() && index < line.length()) {
                    nextChar = line.charAt(index);
                    increment = 1;
                    if(nextChar == '\\') {
                        increment++;
                    }
                    lexeme += line.substring(index, index + increment);
                    line = line.substring(index + increment - 1);
                    if(nextChar == '"') {
                        line = line.substring(1);
                        break;
                    }
                }
            } else if(firstChar == '\'') {
                tokenClass = "CHAR_LITERAL";
                if(line.charAt(1) == '\\') {
                    lexeme = line.substring(0, 4);
                    line = line.substring(4);
                } else {
                    lexeme = line.substring(0, 3);
                    line = line.substring(3);
                }
            } else if(isDigit(firstChar)) {     // Checks for double and float literals too
                tokenClass = "INT_LITERAL";
                while(getTokenClass(line.charAt(index)).equals("UNKNOWN")) {
                    index++;
                }
                lexeme = line.substring(0, index);
                line = line.substring(index);
                char lastChar = lexeme.charAt(lexeme.length() - 1);
                if(lastChar == 'd' || lastChar == 'D') {
                    tokenClass = "DOUBLE_LITERAL";
                } else if(lastChar == 'f' || lastChar == 'F') {
                    tokenClass = "FLOAT_LITERAL";
                } else if(lexeme.contains(".")) {
                    tokenClass = "DOUBLE_LITERAL";
                }
            } else if(isLetter(firstChar)) {
                tokenClass = "IDENT";
                while(getTokenClass(line.charAt(index)).equals("UNKNOWN")) {
                    index++;
                }
                lexeme = line.substring(0, index);
                line = line.substring(index);
                for(String keyword : KEYWORDS) {
                    if(lexeme.equals(keyword)) {
                        tokenClass = "KEYWORD";
                        return;     // Skip the other code since keywords aren't identifiers
                    }
                }
                boolean unique = true;
                for(int i = 0 ; i < identifiers.toArray().length ; i++) {
                    if(lexeme.equals(identifiers.get(i))) {
                        unique = false;
                        break;
                    }
                }
                if(unique) {
                    identifiers.add(lexeme);
                }
            } else {
                tokenClass = "ERROR! SOMETHING IN buildLexeme() FUCKED UP ;_;";
            }
        } else {
            lexeme += firstChar;
            line = line.substring(1);
        }
    }

    private static String getTokenClass(char firstChar) {
        firstChar = (firstChar + "").toLowerCase().charAt(0);
        switch(firstChar) {
            case '(':
                return "LEFT_PAREN";
            case ')':
                return "RIGHT_PAREN";
            case '{':
                return "LEFT_BRACE";
            case '}':
                return "RIGHT_BRACE";
            case '[':
                return "LEFT_BRACK";
            case ']':
                return "RIGHT_BRACK";
            case '=':
                return "ASS_OP";
            case '+':
                return "ADD_OP";
            case '-':
                return "SUB_OP";
            case '*':
                return "MULT_OP";
            case '/':
                return "DIV_OP";
            case '!':
                return "BOOL_NOT";
            case '.':
                return "PERIOD";
            case ',':
                return "COMMA";
            case ';':
                return "SEMICOLON";
            case ' ':
                return "SPACE";     // Unreachable, but buildLexeme uses this to find the end of number literals
            default:
                return "UNKNOWN";
        }
    }

    private static boolean isDigit(char input) {
        try {
            Integer.parseInt(input + "");
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private static boolean isLetter(char input) {
        String temp = (input + "").toLowerCase();
        return "abcdefghijklmnopqrstuvwxyz".contains(temp);
    }
}
