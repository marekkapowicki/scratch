package pl.marekk.scratch.domain;

import pl.marekk.scratch.domain.board.Board;
import pl.marekk.scratch.domain.summary.Reward;
import pl.marekk.scratch.domain.summary.RewardDto;

import java.util.Map;

public class GameResultDto {
    private final String[][] matrix;
    private final Double reward;
    private final Map<String, String> appliedWinningCombinations;
    private final String appliedBonusSymbol;

    private GameResultDto(String[][] matrix, Double reward, Map<String, String> appliedWinningCombinations, String appliedBonusSymbol) {
        this.matrix = matrix;
        this.reward = reward;
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.appliedBonusSymbol = appliedBonusSymbol;
    }


    public static GameResultDto gameResultDto(Board board, Reward reward) {
        final RewardDto rewardDto = reward.toDto();
        return new GameResultDto(board.toDto().matrix(), rewardDto.reward(), rewardDto.appliedWinningCombinations(), rewardDto.appliedBonusSymbol());
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public Double getReward() {
        return reward;
    }

    public Map<String, String> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }
}


