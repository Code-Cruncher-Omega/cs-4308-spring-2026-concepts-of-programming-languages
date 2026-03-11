
import java.util.List;

public class PythonParser {
    private static List<String> tokens;
    private static List<String> lexemes;

    private PythonParser() {}

    public static String parse(String fileName) {
        return parse(LexicalAnalyzer.analyze(fileName));
    }

    public static String parse(LexerResults lexerResults) {
        if(lexerResults == null) { return ""; }

        tokens = lexerResults.getTOKENS();
        lexemes = lexerResults.getLEXEMES();

        return parse(0);
    }

    private static String parse(int index) {   // Used whenever a non-terminal has not been encountered yet.
        String token = tokens.get(index);
        int difference = 0;
        String previousLexeme = "Current index: " + index;  // Initialized to something not blank; used index for debugging.
        String currentLexeme = lexemes.get(index);

        if(index > 0) {
            int tabs = countTabs(currentLexeme);
            previousLexeme = lexemes.get(index - 1);
            int previousTabs = countTabs(previousLexeme);
            previousLexeme = removeTabs(previousLexeme);
            difference = Math.abs(tabs - previousTabs);

            // Allows nested if-statements to work.
            if(tabs - previousTabs > 1 || (tabs - previousTabs == 1 && !previousLexeme.equals(":"))) {
                return "Syntax Error: Too much indentation.";
            }
        }

        currentLexeme = removeTabs(currentLexeme);

        // If the previous lexeme was blank, then it was originally an if, elif, or else.
        if(currentLexeme.equals(":") && !previousLexeme.isBlank()) {
            return "Syntax Error: \"" + token + "\" found but no if_stmt, elif_stmt, or else_block before it.";
        }

        if(tokens.get(index).equals("KEYWORD")) {
            String lexeme = lexemes.get(index);
            String shortened = removeTabs(lexeme);
            // Essentially, the lexeme is set blank as to not reread the same non-terminals for checking nested
            // if, elif, or else's.
            if(shortened.equals("if")) {
                lexemes.set(index, stringOfTabs(countTabs(lexeme)));

                return parse(index + 1, "named_expression");
            } else if(shortened.equals("elif") && difference == 0) {
                return "Syntax Error: \"elif\" found, but no if_stmt before it.";
            } else if(shortened.equals("else") && difference == 0) {
                return "Syntax Error: \"else\" found, but no if_stmt or elif_stmt before it.";
            }
        }

        if(token.equals("EOF")) {
            return "THE CODE SAMPLE IS ACCEPTED-\n(THE LANGUAGE IS ACCEPTED BY THE GRAMMAR)";
        } else {
            return parse(index + 1);
        }
    }

    private static String parse(int index, String expectation) {   // Used after if, elif, or else has been encountered
        String lexeme = lexemes.get(index);
        String shortenedLexeme = removeTabs(lexeme);
        String originalLexeme = lexeme;
        lexeme = shortenedLexeme;
        String token = tokens.get(index);

        boolean lexemeIsNumber = token.equals("INT_LITERAL") || token.equals("FLOAT_LITERAL") || token.equals("IDENT");

        // named_expression is the precondition; assuming it can only be one expression, since the word expression is singular.
        if(expectation.equals("named_expression")) {
            if(lexemeIsNumber) { return parse(index + 1, "boolean_operator"); }

            // If a value is not found for the first token after a conditional, then error.
            return "Syntax Error: Expected \"" + expectation + "\", but found \"" + lexeme + "\".";
        }

        if(expectation.equals("boolean_operator")) {
            if(token.contains("OP")) {
                if("*/+-".contains(lexeme)) {
                    return "Syntax Error: Expected \"" + expectation + ",\", but found \"" + lexeme + "\".";
                }

                // Check if it is "==" or "!=".
                if(lexeme.equals("=") || lexeme.equals("!")) {
                    return parse(index + 1, "equals");
                }

                // The alternative is that it is either "<" or ">".
                return parse(index + 1, "equals_value");
            }
            return "Syntax Error: Expected \"" + expectation + "\", but found \"" + lexeme + "\".";
        }

        if(expectation.contains("equals") && lexeme.equals("=")) {
            return parse(index + 1, "value");
        }

        if(expectation.contains("value")) {
            if(lexemeIsNumber) {
                return parse(index + 1, "colon");
            }
            return "Syntax Error: Expected \"INT_LITERAL\", \"FLOAT_LITERAL\", or \"IDENT\", but found \"" + lexeme + "\".";
        }

        if(expectation.equals("colon")) {
            if(lexeme.equals(":")) {
                return parse(index + 1, "block");
            }
            return "Syntax Error: Expected \"COLON\", but found \"" + lexeme + "\".";
        }

        if(expectation.contains("block")) {
            if(token.equals("EOF")) {
                return "THE CODE SAMPLE IS ACCEPTED-\n(THE LANGUAGE IS ACCEPTED BY THE GRAMMAR)";
            }

            int tabs = countTabs(originalLexeme);
            // Because of the nature of the lexer, all "blocks" must have at least 1 \t and a preceding line.
            String previousLexeme = lexemes.get(index - 1);
            int previousTabs = countTabs(previousLexeme);

            if(tabs == previousTabs) {
                switch(lexeme) {
                    case "if" -> {
                        String result = parse(index);
                        if (result.contains("Error")) {
                            return result;
                        }
                        return parse(index + 1, expectation);
                    }
                    case "elif" -> {
                        return "Syntax Error: Unexpected \"elif\" with no \"if\" before it.";
                    }
                    case "else" -> {
                        return "Syntax Error: Unexpected \"else\" with no \"if\" or \"elif\" before it.";
                    }
                }

                // If the previous lexeme was blank, then it was originally an if, elif, or else.
                if(lexeme.equals(":") && !previousLexeme.isBlank()) {
                    return "Syntax Error: \"" + token + "\" found but no if_stmt, elif_stmt, or else_block before it.";
                }

                return parse(index + 1, expectation);
            }

            int difference = Math.abs(previousTabs - tabs);

            if(tabs < previousTabs) {
                if(difference == 1) {
                    if(expectation.contains("else")) {
                        if(lexeme.equals("elif") || lexeme.equals("else")) { // Not expected in this case.
                            return "Syntax Error: Was not expecting \"" + lexeme + "\" after else_block";
                        }
                    } else {
                        if(lexeme.equals("elif")) { // Same idea as if_stmt, but can only trigger here.
                            lexemes.set(index, stringOfTabs(tabs)); // Essentially, the lexeme is set blank as to not
                                                                    // reread the same non-terminals for checking nested
                                                                    // if, elif, or else's.
                            return parse(index + 1, "named_expression");
                        }
                        if(lexeme.equals("else")) {
                            lexemes.set(index, stringOfTabs(tabs));
                            return parse(index + 1, "else_block");
                        }
                    }
                }
                // If no associated elif or else (i.e. difference > 1), then go back to not expecting them and recheck index.
                return parse(index);
            }

            // tabs > previousTabs
            if(difference == 1) {
                previousLexeme = removeTabs(previousLexeme);
                if(previousLexeme.equals(":")) {    // For nested if, elif, or else's, or first lexeme of the block.
                    return parse(index + 1, expectation);
                }
            }

            return "Syntax Error: Too much indentation.";
        }

        return "Syntax Error: Expected \"ASS_OP\", but found \"" + lexeme + "\".";
    }

    private static int countTabs(String temp) {
        if(temp.startsWith("\t")) { return 1 + countTabs(temp.substring(1)); }

        return 0;
    }

    private static String removeTabs(String temp) {
        if(temp.startsWith("\t")) { return removeTabs(temp.substring(1)); }

        return temp;
    }

    private static String stringOfTabs(int tabs) {
        if(tabs > 0) { return stringOfTabs(tabs - 1, "\t"); }

        return "";
    }

    private static String stringOfTabs(int tabs, String temp) {
        if(tabs > 0) { return stringOfTabs(tabs - 1, "\t" + temp); }

        return temp;
    }
}
