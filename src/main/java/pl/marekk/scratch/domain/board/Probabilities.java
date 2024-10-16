package pl.marekk.scratch.domain.board;

import pl.marekk.scratch.common.Range;
import pl.marekk.scratch.exception.Exceptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static pl.marekk.scratch.domain.board.Probabilities.BonusProbabilities.bonusProbabilities;
import static pl.marekk.scratch.domain.board.Probabilities.CellProbability.cellStandardSymbolProbability;
import static pl.marekk.scratch.domain.board.Probabilities.CellStandardSymbolProbabilities.hashingKeyFunction;
import static pl.marekk.scratch.domain.board.Probabilities.CellStandardSymbolProbabilities.symbolProbabilities;
import static pl.marekk.scratch.domain.board.Probabilities.StandardProbabilities.standardProbabilities;
import static pl.marekk.scratch.exception.ParametersPreconditions.checkNotNull;

public class Probabilities {
    private final StandardProbabilities standardProbabilities;
    private final BonusProbabilities bonusProbabilities;

    private Probabilities(StandardProbabilities standardProbabilities, BonusProbabilities bonusProbabilities) {
        this.standardProbabilities = standardProbabilities;
        this.bonusProbabilities = bonusProbabilities;
    }

    public static Probabilities probabilities(List<CellStandardProbabilityInputParam> standardInputParams, Map<String, Integer> bonusSymbols) {
        checkNotNull(standardInputParams, "problem with standard probabilities");
        checkNotNull(bonusSymbols, "problem with bonus symbols");
        return new Probabilities(standardProbabilities(standardInputParams), bonusProbabilities(bonusSymbols));
    }

    SymbolsInCellProbabilities findBonusSymbolProbabilities() {
        return bonusProbabilities.symbolsInCellProbabilities;
    }

    SymbolsInCellProbabilities findStandardSymbolsProbabilitiesForCell(int row, int column) {
        return standardProbabilities.findStandardSymbolsProbabilitiesForCell(row, column).symbolsInCellProbabilities;
    }

    static class StandardProbabilities {
        private final Map<String, CellStandardSymbolProbabilities> cellProbabilities;

        StandardProbabilities(Map<String, CellStandardSymbolProbabilities> cellProbabilities) {
            this.cellProbabilities = cellProbabilities;
        }

        static StandardProbabilities standardProbabilities(List<CellStandardProbabilityInputParam> inputParams) {
            checkNotNull(inputParams, "problem with creating standard probabilities. input list is null");
            final Map<String, CellStandardSymbolProbabilities> probabilitiesMap = inputParams.stream()
                    .map(input -> symbolProbabilities(input.row, input.column, input.symbols))
                    .collect(Collectors.toMap(CellStandardSymbolProbabilities::getPosition, Function.identity()));
            return new StandardProbabilities(probabilitiesMap);
        }

        CellStandardSymbolProbabilities findStandardSymbolsProbabilitiesForCell(int row, int column) {
            final String positionKey = hashingKeyFunction.apply(row, column);
            final CellStandardSymbolProbabilities cellStandardSymbolProbabilities = cellProbabilities.get(positionKey);
            checkNotNull(cellStandardSymbolProbabilities, "no probability found for cell: " + positionKey);
            return cellStandardSymbolProbabilities;
        }

    }

    public record CellStandardProbabilityInputParam(Integer row, Integer column, Map<String, Integer> symbols) {

    }

    static class CellStandardSymbolProbabilities {
        static final BiFunction<Integer, Integer, String> hashingKeyFunction = (row, column) -> row + "_" + column;
        private final String position;
        private final SymbolsInCellProbabilities symbolsInCellProbabilities;

        private CellStandardSymbolProbabilities(String position, SymbolsInCellProbabilities symbolsInCellProbabilities) {
            checkNotNull(symbolsInCellProbabilities, "probabilities for cell: " + position + " are null");
            this.position = position;
            this.symbolsInCellProbabilities = symbolsInCellProbabilities;

        }

        static CellStandardSymbolProbabilities symbolProbabilities(Integer row, Integer column, Map<String, Integer> symbols) {
            final String positionKey = hashingKeyFunction.apply(row, column);
            return new CellStandardSymbolProbabilities(positionKey, SymbolsInCellProbabilities.symbolsInCellProbabilities(symbols));
        }

        String getPosition() {
            return position;
        }

    }


    static class CellProbability {
        private final String symbol;
        private final Range<Integer> range;

        private CellProbability(String symbol, Range<Integer> range) {
            this.symbol = symbol;
            this.range = range;
        }

        static CellProbability cellStandardSymbolProbability(String symbol, int probabilityMinRangeInclusive, int probabilityMaxRangeExclusive) {
            return new CellProbability(symbol, Range.between(probabilityMinRangeInclusive, probabilityMaxRangeExclusive));
        }

        String symbol() {
            return symbol;
        }

        Range<Integer> range() {
            return range;
        }
    }


    static class BonusProbabilities {
        private final SymbolsInCellProbabilities symbolsInCellProbabilities;

        private BonusProbabilities(SymbolsInCellProbabilities symbolsInCellProbabilities) {
            this.symbolsInCellProbabilities = symbolsInCellProbabilities;
        }

        static BonusProbabilities bonusProbabilities(Map<String, Integer> symbols) {
            return new BonusProbabilities(SymbolsInCellProbabilities.symbolsInCellProbabilities(symbols));
        }
    }

    public static class SymbolsInCellProbabilities {
        private final List<CellProbability> symbolProbabilities;
        private final int maxRangeExclusive;

        private SymbolsInCellProbabilities(List<CellProbability> symbolProbabilities, int maxRangeExclusive) {
            this.symbolProbabilities = symbolProbabilities;
            this.maxRangeExclusive = maxRangeExclusive;
        }

        static SymbolsInCellProbabilities symbolsInCellProbabilities(Map<String, Integer> symbols) {
            final Iterator<Map.Entry<String, Integer>> iterator = symbols.entrySet().iterator();
            final List<CellProbability> probabilitiesIsCell = new ArrayList<>();
            int minRange = 0;
            int globalMax = 0;
            while (iterator.hasNext()) {
                final Map.Entry<String, Integer> entry = iterator.next();
                final int maxRange = minRange + entry.getValue();
                probabilitiesIsCell.add(cellStandardSymbolProbability(entry.getKey(), minRange, maxRange));
                minRange = maxRange;
                globalMax = max(globalMax, maxRange);
            }

            return new SymbolsInCellProbabilities(probabilitiesIsCell, globalMax);
        }

        CellProbability findSymbolOnPositionBasedOnGeneratedValue(int generatedNumber) {
            return symbolProbabilities.stream()
                    .filter(potentialSymbol -> potentialSymbol.range.isElementIn(generatedNumber))
                    .findFirst()
                    .orElseThrow(() -> Exceptions.illegalState("exception during building the cell: "));
        }

        Range<Integer> numberToDrawRange() {
            return Range.between(0, maxRangeExclusive);
        }
    }
}
