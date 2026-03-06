
import java.util.List;

public class PythonParser {
    private static List<String> tokens;
    private static List<String> lexemes;

    private PythonParser() {}

    public String parse(String fileName) {
        return parse(LexicalAnalyzer.analyze(fileName));
    }

    public static String parse(LexerResults lexerResults) {
        tokens = lexerResults.getTOKENS();
        lexemes = lexerResults.getLEXEMES();

        return parse(0);
    }

    private static String parse(int index) {   // Used whenever a non-terminal has not been encountered yet
        String token = tokens.get(index);

        if(index != 0) {
            int tabs = countTabs(lexemes.get(index));
            String previousLexeme = lexemes.get(index - 1);
            int previousTabs = countTabs(previousLexeme);
            if(tabs - previousTabs > 0) {
                return "Syntax Error: Too much indentation.";
            }
            if(previousTabs - tabs > 0) {
                return "Syntax Error: Too little indentation.";
            }
        }
        if(token.equals("EOF")) {
            return "THE CODE SAMPLE IS ACCEPTED-\n(THE LANGUAGE IS ACCEPTED BY THE GRAMMAR)";
        }
        if(tokens.get(index).equals("KEYWORD")) {
            String lexeme = lexemes.get(index);
            switch(lexeme) {
                case "if" -> {  // if_stmt found.
                    return parse(index + 1, "named_expression");
                }
                case "elif" -> {
                    return "Syntax Error: Found \"elif\", but no \"if\" before it.";
                }
                case "else" -> {
                    return "Syntax Error: Found \"else\", but no \"if\" or \"elif\" before it.";
                }
            }
        }
        return parse(index + 1);
    }

    private static String parse(int index, String expectation) {   // Used after "if" or "elif" has been encountered
        String lexeme = lexemes.get(index);
        String shortenedLexeme = lexeme;
        while(shortenedLexeme.startsWith("\t")) {
            shortenedLexeme = shortenedLexeme.substring(1);
        }
        String originalLexeme = lexeme;
        lexeme = shortenedLexeme;
        String token = tokens.get(index);

        boolean lexemeIsNumber = token.equals("INT_LITERAL") || token.equals("FLOAT_LITERAL") || token.equals("IDEN");
        if(expectation.equals("named_expression")) {    // The precondition; assuming it can only be one expression,
                                                        // since the word expression is singular.
            if(lexemeIsNumber) {
                return parse(index + 1, "boolean_operator");
            }
            // If a value is not found for the first token after a conditional, then error.
            return "Syntax Error: Expected \"NAMED_EXPRESSION\", but found \"" + lexeme + "\".";
        }
        if(expectation.equals("boolean_operator")) {
            if(token.contains("OP")) {
                if("*/+-".contains(lexeme)) {
                    return "Syntax Error: Expected \"BOOLEAN_OPERATOR\", but found \"" + lexeme + "\".";
                }
                // Check if it is "==" or "!=".
                if(lexeme.equals("=") || lexeme.equals("!")) {
                    return parse(index + 1, "equals");
                }
                // The alternative is that it is either "<" or ">"
                return parse(index + 1, "equals_value");
            }
            return "Syntax Error: Expected \"BOOLEAN_OPERATOR\", but found \"" + lexeme + "\".";
        }
        if(expectation.contains("equals") && lexeme.equals("=")) {
            return parse(index + 1, "value");
        }
        if(expectation.contains("value")) {
            if(lexemeIsNumber) {
                return parse(index + 1, "colon");
            }
            return "Syntax Error: Expected \"INT_LITERAL\", \"FLOAT_LITERAL\", or \"IDEN\", but found \"" + lexeme + "\".";
        }
        if(expectation.equals("colon")) {
            if(lexeme.equals(":")) {
                return parse(index + 1, "block");
            }
            return "Syntax Error: Expected \"COLON\", but found \"" + lexeme + "\".";
        }
        if(expectation.equals("block")) {
            if(lexeme.equals("elif")) {

            }
            if(lexeme.equals("else")) {
                
            }
            int tabs = countTabs(originalLexeme);
            // Because of the nature of the lexer, all "blocks" must have at least 1 \t and a preceding line.
            String previousLexeme = lexemes.get(index - 1);
            int previousTabs = countTabs(previousLexeme);
            if(tabs == previousTabs) {
                return parse(index + 1, expectation);
            }
            if(tabs < previousTabs) {
                return parse(index + 1);   // If no elif or else, then go back to not expecting them.
            }
            if(tabs - previousTabs > 1 || !previousLexeme.equals(":")) {
                return "Syntax Error: Too much indentation.";
            }
        }
        // Checks if the next lexeme is END_OF_FILE, elif, or else
        if(expectation.contains(lexeme)) {
            if (lexeme.equals("END_OF_FILE")) {
                return "THE CODE SAMPLE IS ACCEPTED-\n(THE LANGUAGE IS ACCEPTED BY THE GRAMMAR)";
            }
            if (lexeme.equals("elif")) {
                return parse(index + 1, "named_expression");
            }
            if (lexeme.equals("else")) {
                return parse(index + 1, "block");
            }
        }

        return "Syntax Error: Expected \"ASS_OP\", but found \"" + lexeme + "\".";
    }

    private static int countTabs(String temp) {
        if(temp.startsWith("\t")) {
            return 1 + countTabs(temp.substring(1));
        }
        return 0;
    }
}
