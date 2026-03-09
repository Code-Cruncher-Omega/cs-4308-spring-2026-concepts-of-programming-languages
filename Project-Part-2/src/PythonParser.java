
import java.util.List;

public class PythonParser {
    private static List<String> tokens;
    private static List<String> lexemes;

    private PythonParser() {}

    public static String parse(String fileName) {
        return parse(LexicalAnalyzer.analyze(fileName));
    }

    public static String parse(LexerResults lexerResults) {
        tokens = lexerResults.getTOKENS();
        lexemes = lexerResults.getLEXEMES();

        return parse(0);
    }

    private static String parse(int index) {   // Used whenever a non-terminal has not been encountered yet
        String token = tokens.get(index);

        if(index > 0) {
            int tabs = countTabs(lexemes.get(index));
            String previousLexeme = lexemes.get(index - 1);
            int previousTabs = countTabs(previousLexeme);
            // Allows nested if-statements to work.
            if(tabs - previousTabs > 1 || (tabs - previousTabs == 1 && !previousLexeme.equals(":"))) {
                return "Syntax Error: Too much indentation.";
            }
            if(previousTabs > tabs) {
                return "Syntax Error: Too little indentation.";
            }
        }
        if(tokens.get(index).equals("KEYWORD")) {
            String lexeme = lexemes.get(index);
            String shortened = removeTabs(lexeme);
            if(shortened.equals("if")) {
                lexemes.set(index, stringOfTabs(countTabs(lexeme)));// Essentially, the lexeme is set blank as to not
                                                                    // reread the same non-terminals for checking nested
                                                                    // if, elif, or else's.
                return parse(index + 1, "named_expression");
            } else if(shortened.equals("elif")) {
                return "Syntax Error: Found \"elif\", but no \"if\" before it.";
            } else if(shortened.equals("else")) {
                return "Syntax Error: Found \"else\", but no \"if\" or \"elif\" before it.";
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
        if(expectation.equals("named_expression")) {    // The precondition; assuming it can only be one expression,
                                                        // since the word expression is singular.
            if(lexemeIsNumber) {
                return parse(index + 1, "boolean_operator");
            }
            // If a value is not found for the first token after a conditional, then error.
            return "Syntax Error: Expected \"named_expression\", but found \"" + lexeme + "\".";
        }
        if(expectation.equals("boolean_operator")) {
            if(token.contains("OP")) {
                if("*/+-".contains(lexeme)) {
                    return "Syntax Error: Expected \"boolean_operator\", but found \"" + lexeme + "\".";
                }
                // Check if it is "==" or "!=".
                if(lexeme.equals("=") || lexeme.equals("!")) {
                    return parse(index + 1, "equals");
                }
                // The alternative is that it is either "<" or ">"
                return parse(index + 1, "equals_value");
            }
            return "Syntax Error: Expected \"boolean_operator\", but found \"" + lexeme + "\".";
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
                if(lexeme.equals("if")) {   // Checks if nested if-statements are valid.
                    String result = parse(index);
                    if(result.contains("Error")) {
                        return result;
                    }
                    return parse(index + 1, expectation);
                }
                return parse(index + 1, expectation);
            }
            int difference = previousTabs - tabs;
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
                if(previousLexeme.equals(":")) {    // For nested if, elif, or else's, or first lexeme of the block.
                    return parse(index + 1, expectation);
                }
            }

            return "Syntax Error: Too much indentation.";
        }

        return "Syntax Error: Expected \"ASS_OP\", but found \"" + lexeme + "\".";
    }

    private static int countTabs(String temp) {
        if(temp.startsWith("\t")) {
            return 1 + countTabs(temp.substring(1));
        }
        return 0;
    }

    private static String removeTabs(String temp) {
        if(temp.startsWith("\t")) {
            return removeTabs(temp.substring(1));
        }
        return temp;
    }

    private static String stringOfTabs(int tabs) {
        if(tabs > 0) {
            return stringOfTabs(tabs - 1, "\t");
        }
        return "";
    }

    private static String stringOfTabs(int tabs, String temp) {
        if(tabs > 0) {
            return stringOfTabs(tabs - 1, "\t" + temp);
        }
        return temp;
    }
}
