package pl.marekk.scratch.domain.board;

import pl.marekk.scratch.common.RandomGenerator;
import pl.marekk.scratch.domain.Symbol;
import pl.marekk.scratch.domain.SymbolsDictionary;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Board {
    private final String[][] symbols;

    private final BonusCell bonusCell;

    private Board(String[][] symbols, BonusCell bonusCell) {
        this.symbols = symbols;
        this.bonusCell = bonusCell;
    }

    static Board board(String[][] symbols, BonusCell bonusCell) {
        return new Board(symbols, bonusCell);
    }


    public BoardDto toDto() {
        return new BoardDto(symbols);
    }

    @Override
    public String toString() {
        final List<String> rows = Arrays.stream(symbols).toList()
                .stream()
                .map(Arrays::toString)
                .toList();
        return "[" + String.join("\n", rows) + "]";
    }


    public Map<String, Long> countStandardSymbols() {
        return Arrays.stream(symbols)
                .flatMap(Arrays::stream)
                .filter(bonusCell::nonBonus)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Symbol bonusSymbol() {
        return bonusCell.symbol();
    }

    record BonusCell(int bonusRow, int bonusColumn, Symbol symbol) {


        static BonusCell bonusCell(int rowsNo, int columnsNo, BoardCellFactory cellFactory, SymbolsDictionary symbolsDictionary) {
            final int bonusRow = RandomGenerator.randomNumberInRange(0, rowsNo);
            final int columnRow = RandomGenerator.randomNumberInRange(0, columnsNo);
            final String bonusValue = cellFactory.generateBonusSymbolForCell();
            return new BonusCell(bonusRow, columnRow, symbolsDictionary.getSymbol(bonusValue));
        }

        boolean nonBonus(String symbol) {
            return !value().equals(symbol);
        }

        String value() {
            return symbol.name();
        }

        boolean isBonusCell(int row, int column) {
            return bonusRow == row && bonusColumn == column;
        }
    }
}
