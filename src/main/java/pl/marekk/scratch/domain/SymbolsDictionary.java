package pl.marekk.scratch.domain;

import pl.marekk.scratch.exception.Exceptions;
import pl.marekk.scratch.exception.ParametersPreconditions;

import java.util.Map;
import java.util.Set;

public class SymbolsDictionary {
    private final Map<String, Symbol> dictionary;

    private SymbolsDictionary(Map<String, Symbol> dictionary) {
        this.dictionary = dictionary;
    }

    public static SymbolsDictionary symbolsDictionary(Map<String, Symbol> dictionary) {
        ParametersPreconditions.checkNotNull(dictionary, "dictionary of symbols in null");
        return new SymbolsDictionary(dictionary);

    }

    public Symbol getSymbol(String name) {
        final Symbol symbol = dictionary.get(name);
        if (symbol == null) {
            throw Exceptions.illegalArgument("invalid symbol: " + name);
        }
        return symbol;
    }

    public Set<String> allSymbolsNames() {
        return dictionary.keySet();
    }
}
