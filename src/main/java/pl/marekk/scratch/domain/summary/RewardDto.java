package pl.marekk.scratch.domain.summary;

import java.util.Map;

public record RewardDto(Double reward, Map<String, String> appliedWinningCombinations, String appliedBonusSymbol) {
}