package pl.marekk.scratch.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.scratch.domain.board.Board;
import pl.marekk.scratch.domain.board.BoardFactory;
import pl.marekk.scratch.domain.board.Probabilities;
import pl.marekk.scratch.domain.summary.ResultsAnalyzer;
import pl.marekk.scratch.domain.summary.Reward;
import pl.marekk.scratch.exception.ParametersPreconditions;

public class Game {
    private static final Logger logger
            = LoggerFactory.getLogger(Game.class);
    private final BoardFactory boardFactory;
    private final ResultsAnalyzer resultsAnalyzer;

    private final int columns;
    private final int rows;

    private Game(BoardFactory boardFactory, ResultsAnalyzer resultsAnalyzer, int columns, int rows) {
        this.boardFactory = boardFactory;
        this.resultsAnalyzer = resultsAnalyzer;
        this.columns = columns - 1;
        this.rows = rows - 1;
    }

    public static Game createNewGame(SymbolsDictionary symbolsDictionary, Probabilities probabilities, ResultsAnalyzer resultsAnalyzer, int columns, int rows) {
        ParametersPreconditions.checkArgument(rows > 0, "number of rows in board has to be positive");
        ParametersPreconditions.checkArgument(columns > 0, "number of column in board has to be positive");
        return new Game(BoardFactory.boardFactory(probabilities, symbolsDictionary), resultsAnalyzer, columns, rows);
    }


    public GameResultDto playTheGame(Double bettingAmount) {
        Board board = buildBoard();
        logger.debug("generated board \n{}\n", board);
        final Reward reward = resultsAnalyzer.checkResults(board, bettingAmount);
        logger.debug("reward {}", reward);
        return GameResultDto.gameResultDto(board, reward);
    }

    private Board buildBoard() {
        return boardFactory.genarateBoard(rows, columns);
    }
}
