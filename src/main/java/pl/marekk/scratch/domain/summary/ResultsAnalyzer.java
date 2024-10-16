package pl.marekk.scratch.domain.summary;

import pl.marekk.scratch.domain.board.Board;

import java.util.List;

public class ResultsAnalyzer {
    private final WinningStrategies winningStrategies;

    private ResultsAnalyzer(WinningStrategies winningStrategies) {
        this.winningStrategies = winningStrategies;
    }

    public static ResultsAnalyzer resultsAnalyzer(List<WinningStrategyInputParameter> winningStrategyInputParameters) {
        return new ResultsAnalyzer(WinningStrategies.winningStrategies(winningStrategyInputParameters));
    }

    public Reward checkResults(final Board board, Double bettingAmount) {
        final List<WinningCombination> winningCombinations = winningStrategies.findWinningCombinationsForBoard(board);
        return Reward.RewardBuilder.rewardBuilder(winningCombinations)
                .applyBonusSymbolIfEnabled(board.bonusSymbol()).build(bettingAmount);
    }
}
