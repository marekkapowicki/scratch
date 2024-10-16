package pl.marekk.scratch.application;


import pl.marekk.scratch.domain.Symbol;
import pl.marekk.scratch.domain.SymbolsDictionary;
import pl.marekk.scratch.domain.board.Probabilities;
import pl.marekk.scratch.domain.summary.ResultsAnalyzer;
import pl.marekk.scratch.domain.summary.WinningStrategyInputParameter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.marekk.scratch.exception.ParametersPreconditions.checkNotNull;

record JsonGameConfiguration(Integer columns, Integer rows, Map<String, ConfigSymbol> symbols,
                             ConfigProbabilities probabilities,
                             Map<String, WinCombination> winCombinations) {

    ResultsAnalyzer toResults() {
        final List<WinningStrategyInputParameter> winningStrategyInputParameters = winCombinations.entrySet().stream()
                .map(entry -> entry.getValue().toWinningStrategyInputParameter(entry.getKey()))
                .toList();
        return ResultsAnalyzer.resultsAnalyzer(winningStrategyInputParameters);
    }

    SymbolsDictionary toSymbolsDictionary() {
        checkNotNull(symbols, "list of symbols is null");
        final Map<String, Symbol> symbolsDictionary = symbols.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().toDomainSymbol(e.getKey())));
        return SymbolsDictionary.symbolsDictionary(symbolsDictionary);
    }

    Probabilities toProbability() {
        return probabilities.toProbabilities();
    }

    enum SymbolType {
        standard, bonus
    }

    record ConfigSymbol(Double rewardMultiplier, SymbolType type, Double extra, String impact) {
        Symbol toDomainSymbol(String name) {
            return Symbol.newSymbol(type.name(), new Symbol.InputSymbolParameters(name, rewardMultiplier, extra, impact));
        }
    }

    record ConfigProbabilities(List<ProbabilityStandardSymbol> standardSymbols, ProbabilityBonusSymbols bonusSymbols) {

        Probabilities toProbabilities() {
            final List<Probabilities.CellStandardProbabilityInputParam> standardProbabilityInputParams = standardSymbols.stream()
                    .map(ProbabilityStandardSymbol::toInputParam)
                    .toList();
            return Probabilities.probabilities(standardProbabilityInputParams, bonusSymbols.symbols());


        }
    }

    record ProbabilityStandardSymbol(Integer row, Integer column, Map<String, Integer> symbols) {

        Probabilities.CellStandardProbabilityInputParam toInputParam() {
            return new Probabilities.CellStandardProbabilityInputParam(row, column, symbols);
        }
    }

    record ProbabilityBonusSymbols(Map<String, Integer> symbols) {
    }

    record WinCombination(Double rewardMultiplier, String when, Integer count, String group,
                          List<List<String>> coveredAreas) {
        WinningStrategyInputParameter toWinningStrategyInputParameter(String name) {
            return new WinningStrategyInputParameter(name, rewardMultiplier, when, count, group, coveredAreas);
        }

    }
}
