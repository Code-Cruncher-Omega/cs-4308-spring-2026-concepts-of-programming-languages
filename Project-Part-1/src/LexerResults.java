import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LexerResults {
    private final ArrayList<String> identifiers;
    private final ArrayList<String> tokens;
    private final ArrayList<String> lexemes;

    public LexerResults(ArrayList<String> identifiers, ArrayList<String> tokens, ArrayList<String> lexemes) {
        this.identifiers = identifiers;
        this.tokens = tokens;
        this.lexemes = lexemes;
    }

    public List<String> getIdentifiers() {
        return Collections.unmodifiableList(identifiers);
    }

    public List<String> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public List<String> getLexemes() {
        return Collections.unmodifiableList(lexemes);
    }
}
