package pl.marekk.scratch.domain.summary;

import pl.marekk.scratch.domain.Symbol;
import pl.marekk.scratch.exception.Exceptions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Reward {
    private final List<WinningCombination> appliedWinningCombinations;
    private final Symbol appledBonusSymbol;
    private final Double reward;

    public Reward(List<WinningCombination> appliedWinningCombinations, Symbol appledBonusSymbol, Double reward) {
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.appledBonusSymbol = appledBonusSymbol;
        this.reward = reward;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Reward.class.getSimpleName() + "[", "]")
                .add("appliedWinningCombinations=" + appliedWinningCombinations)
                .add("appledBonusSymbol=" + appledBonusSymbol)
                .add("reward=" + reward)
                .toString();
    }

    public RewardDto toDto() {
        final Map<String, String> appliedCombinationsNames = appliedWinningCombinations.stream()
                .map(WinningCombination::asMapEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final String appliedBonusSymbol = appledBonusSymbol != null ? appledBonusSymbol.name() : null;
        return new RewardDto(reward, appliedCombinationsNames, appliedBonusSymbol);
    }

    static class RewardBuilder {
        private final List<WinningCombination> appliedWinningCombinations;
        private Optional<Symbol.BonusSymbol> optionalAppliedSymbol = Optional.empty();

        private RewardBuilder(List<WinningCombination> appliedWinningCombinations) {
            this.appliedWinningCombinations = appliedWinningCombinations;
        }

        static RewardBuilder rewardBuilder(final List<WinningCombination> appliedWinningCombinations) {
            return new RewardBuilder(appliedWinningCombinations);
        }

        RewardBuilder applyBonusSymbolIfEnabled(Symbol symbol) {
            if (appliedWinningCombinations != null && !appliedWinningCombinations.isEmpty()) {
                if (symbol instanceof Symbol.BonusSymbol bonusSymbol) {
                    this.optionalAppliedSymbol = Optional.of(bonusSymbol);
                } else {
                    throw Exceptions.illegalState("bonus symbol problem: " + symbol);
                }
            }
            return this;
        }

        Reward build(Double betAmount) {
            final Symbol.BonusSymbol appledBonusSymbol = optionalAppliedSymbol.orElseGet(() -> null);
            return new Reward(appliedWinningCombinations, appledBonusSymbol, calculateAmount(betAmount));
        }

        private Double calculateAmount(Double betAmount) {
            final Double standardReward = appliedWinningCombinations.stream()
                    .mapToDouble(it -> it.apply(betAmount))
                    .sum();
            return optionalAppliedSymbol.map(it -> it.applyBonus(standardReward))
                    .orElseGet(() -> standardReward);
        }
    }
}
