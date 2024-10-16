package pl.marekk.scratch.domain.summary;

import java.util.Map;
import java.util.function.Function;

record WinningCombination(String symbol,
                          WinningStrategies.WinningStrategy winningStrategy) implements Function<Double, Double> {

    @Override
    public Double apply(Double amount) {
        return amount * winningStrategy.rewardMultiplier();
    }

    Map.Entry<String, String> asMapEntry() {
        return Map.entry(symbol, winningStrategy.toString());
    }

    @Override
    public String toString() {
        return symbol + ":" + winningStrategy;
    }
}
