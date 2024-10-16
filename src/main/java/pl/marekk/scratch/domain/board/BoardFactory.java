package pl.marekk.scratch.domain.board;

import pl.marekk.scratch.domain.SymbolsDictionary;
import pl.marekk.scratch.exception.ParametersPreconditions;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.marekk.scratch.domain.board.Board.board;
import static pl.marekk.scratch.exception.ParametersPreconditions.checkArgument;

/**
 * use to generate the matrix of symbols
 * based on probabilities
 */
public class BoardFactory {

    private final BoardCellFactory boardCellFactory;
    private final SymbolsDictionary symbolsDictionary;

    private BoardFactory(BoardCellFactory boardCellFactory, SymbolsDictionary symbolsDictionary) {
        this.boardCellFactory = boardCellFactory;
        this.symbolsDictionary = symbolsDictionary;
    }

    public static BoardFactory boardFactory(Probabilities probabilities, SymbolsDictionary symbolsDictionary) {
        return new BoardFactory(BoardCellFactory.boardCellFactory(probabilities), symbolsDictionary);
    }

    public Board genarateBoard(int rowsNo, int columnsNo) {
        final Board.BonusCell randomBonusCell = Board.BonusCell.bonusCell(rowsNo, columnsNo, boardCellFactory, symbolsDictionary);
        final String[][] symbols = generate(rowsNo, columnsNo, randomBonusCell);
        validateSymbols(symbolsDictionary.allSymbolsNames(), symbols);
        return board(symbols, randomBonusCell);
    }

    /**
     * single bonus symbol exists in generated board
     */
    private String[][] generate(int rowsNo, int columnsNo, Board.BonusCell bonusCell) {
        checkArgument(rowsNo > 0, "number of rows has to  be positive");
        checkArgument(columnsNo > 0, "number of columns has to  be positive");

        String[][] matrix = new String[rowsNo][columnsNo];
        for (int row = 0; row < rowsNo; row++) {
            for (int column = 0; column < columnsNo; column++) {
                if (bonusCell.isBonusCell(row, column)) {
                    matrix[row][column] = bonusCell.value();
                } else {
                    matrix[row][column] = boardCellFactory.generateStandardSymbolForCell(row, column);
                }
            }
        }
        return matrix;
    }

    private void validateSymbols(Set<String> allAllowedSymbols, final String[][] symbols) {
        final Set<String> symbolsInBoard = Arrays.stream(symbols)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
        ParametersPreconditions.checkArgument(allAllowedSymbols.containsAll(symbolsInBoard), "unknown Symbols in Board");
    }

}
