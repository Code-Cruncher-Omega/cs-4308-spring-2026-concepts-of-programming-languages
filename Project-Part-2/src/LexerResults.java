import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LexerResults {
    private final ArrayList<String> IDENTIFIERS;
    private final ArrayList<String> TOKENS;
    private final ArrayList<String> LEXEMES;

    public LexerResults(ArrayList<String> identifiers, ArrayList<String> tokens, ArrayList<String> lexemes) {
        IDENTIFIERS = identifiers;
        TOKENS = tokens;
        LEXEMES = lexemes;
    }

    public List<String> getIDENTIFIERS() {
        return Collections.unmodifiableList(IDENTIFIERS);
    }

    public List<String> getTOKENS() {
        return Collections.unmodifiableList(TOKENS);
    }

    public List<String> getLEXEMES() {
        return Collections.unmodifiableList(LEXEMES);
    }
}
