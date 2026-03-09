import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    // Keywords in Python
    private static final List<String> KEYWORDS = List.of(
            "False", "None", "True", "and", "as", "assert", "async",
            "await", "break", "class", "continue", "def", "elif", "else",
            "except", "finally", "for", "from", "global", "if", "import",
            "in", "is", "lambda", "nonlocal", "not", "or", "pass", "raise",
            "return", "try", "while", "with", "yield");
    private static ArrayList<String> identifiers;   // Not used, but stored in case of future use
    private static ArrayList<String> tokens;    // Same size as lexemes arraylist
    private static ArrayList<String> lexemes;

    private static String lexeme = "";
    private static String line;
    private static String tokenClass;

    private static boolean firstWord;
    private static int tabs;

    private LexicalAnalyzer() {}    // Non-instantiable constructor

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
            while(tempLexeme.startsWith("\t")) {
                tempLexeme = tempLexeme.substring(1);
            }
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
            while((line = file.readLine()) != null) { analyzeLine(); }
            tokens.add("EOF");
            lexemes.add("END OF FILE");
            return new LexerResults(identifiers, tokens, lexemes);
        } catch(IOException e) {
            System.out.println("File not found!");
            return null;
        }
    }

    private static void analyzeLine() {
        firstWord = true;
        tabs = 0;
        while(!line.isEmpty()) {
            nextLexeme();
            buildLexeme();
            if(lexeme.isEmpty() || tokenClass.isEmpty()) {
                continue;
            }
            tokens.add(tokenClass);
            for(int currentTabs = 0 ; currentTabs < tabs ; currentTabs++) {
                lexeme = "\t" + lexeme;
            }
            firstWord = false;
            lexemes.add(lexeme);
        }
    }

    private static void nextLexeme() {
        while(line.startsWith(" ")) {
            if(firstWord) { tabs++; }
            line = line.substring(1);
        }
        if(firstWord) { tabs /= 4; }

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
            if(firstChar == '"' || firstChar == '\'') {
                tokenClass = "STR_LITERAL";
                lexeme += firstChar;
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
                    if(nextChar == firstChar) {
                        line = line.substring(1);
                        break;
                    }
                }
            } else if(isDigit(firstChar)) {     // Checks for float literals too
                tokenClass = "INT_LITERAL";
                while(getTokenClass(line.charAt(index)).equals("UNKNOWN")) {
                    index++;
                }
                lexeme = line.substring(0, index);
                line = line.substring(index);
                // e or E indicates exponents which automatically defaults the variable to a float literal in Python
                if(lexeme.contains(".") || lexeme.contains("e") || lexeme.contains("E")) {
                    tokenClass = "FLOAT_LITERAL";
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
        return switch(firstChar) {
            case '(' -> "LEFT_PAREN";
            case ')' -> "RIGHT_PAREN";
            case '{' -> "LEFT_BRACE";
            case '}' -> "RIGHT_BRACE";
            case '[' -> "LEFT_BRACK";
            case ']' -> "RIGHT_BRACK";
            case '=' -> "ASS_OP";
            case '<' -> "LESS_OP";
            case '>' -> "GREAT_OP";
            case '+' -> "ADD_OP";
            case '-' -> "SUB_OP";
            case '*' -> "MULT_OP";
            case '/' -> "DIV_OP";
            case '!' -> "NOT_OP";
            case '.' -> "PERIOD";
            case ',' -> "COMMA";
            case ':' -> "COLON";
            case ';' -> "SEMICOLON";
            case ' ' -> "SPACE";     // Unreachable, but buildLexeme uses this to find the end of number literals
            default -> "UNKNOWN";
        };
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
        // Underscore can be a part of variable names
        return "_abcdefghijklmnopqrstuvwxyz".contains(temp);
    }
}
