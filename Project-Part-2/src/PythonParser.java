import java.util.ArrayList;
import java.util.List;

public class PythonParser {
    private static List<String> tokens;
    private static List<String> lexemes;
    private static List<String> orderOfGrammar;

    private PythonParser() {}

    public void parse(String fileName) {
        parse(LexicalAnalyzer.analyze(fileName));
    }

    public void parse(LexerResults lexerResults) {
        tokens = lexerResults.getTokens();
        lexemes = lexerResults.getLexemes();
        orderOfGrammar = new ArrayList<>();
        parse(0);
    }

    private void parse(int index) {
        if(tokens.get(index).equals("KEYWORD")) {
            if(lexemes.get(index).equals())
        }
    }

    private void parse(int index, String expectation) {

    }
}
