package pl.marekk.scratch.domain.summary;

import pl.marekk.scratch.domain.board.Board;
import pl.marekk.scratch.exception.Exceptions;
import pl.marekk.scratch.exception.ParametersPreconditions;

import java.util.*;
import java.util.function.Function;

import static java.util.Optional.of;

class WinningStrategies {

    private final List<WinningStrategy> winningStrategies;

    private WinningStrategies(List<WinningStrategy> winningStrategies) {
        this.winningStrategies = winningStrategies;
    }

    static WinningStrategies winningStrategies(List<WinningStrategyInputParameter> inputParameter) {
        ParametersPreconditions.checkNotNull(inputParameter, "winning strategy null input");
        final List<WinningStrategy> winningStrategies = inputParameter.stream()
                .map(WinningStrategy::newWinningStrategy).flatMap(Optional::stream).toList();
        return new WinningStrategies(winningStrategies);
    }

    List<WinningCombination> findWinningCombinationsForBoard(Board board) {
        return winningStrategies.stream()
                .map(strategy -> strategy.findWinningCombinations(board))
                .flatMap(List::stream)
                .toList();
    }

    //TODO add more
    enum CombinationGroup {
        SAME_SYMBOLS(input -> of(SameSymbolsStrategy.sameSymbolsStrategy(input))), HORIZONTALLY_LINEAR_SYMBOLS(input -> Optional.of(SameSymbolsStrategy.HorizontallyLinearStrategy.horizontallyLinearStrategy(input))), VERTICALLY_LINEAR_SYMBOLS(input -> Optional.empty()), LTR_DIAGONALLY_LINEAR_SYMBOLS(input -> Optional.empty()), RTL_DIAGONALLY_LINEAR_SYMBOLS(input -> Optional.empty());
        private final Function<WinningStrategyInputParameter, Optional<WinningStrategy>> factoryFunction;

        CombinationGroup(final Function<WinningStrategyInputParameter, Optional<WinningStrategy>> factoryFunction) {
            this.factoryFunction = factoryFunction;
        }

        static CombinationGroup fromString(String name) {
            return Arrays.stream(values()).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> Exceptions.illegalState("invalid group in winning combination: " + name));
        }

        Optional<WinningStrategy> tryCreateWinningStrategy(WinningStrategyInputParameter inputParameter) {
            return factoryFunction.apply(inputParameter);
        }
    }

    interface WinningStrategy {
        static Optional<WinningStrategy> newWinningStrategy(WinningStrategyInputParameter inputParameter) {
            final CombinationGroup combinationGroup = CombinationGroup.fromString(inputParameter.group());
            return combinationGroup.tryCreateWinningStrategy(inputParameter);
        }

        List<WinningCombination> findWinningCombinations(Board board);

        Double rewardMultiplier();

    }


    record SameSymbolsStrategy(String name, String when, Double rewardMultiplier,
                               int count) implements WinningStrategy {
        static SameSymbolsStrategy sameSymbolsStrategy(WinningStrategyInputParameter inputParameter) {
            return new SameSymbolsStrategy(inputParameter.name(), inputParameter.when(), inputParameter.rewardMultiplier(), inputParameter.count());
        }

        @Override
        public String toString() {
            return name();
        }

        @Override
        public List<WinningCombination> findWinningCombinations(Board board) {
            return board.countStandardSymbols().entrySet().stream()
                    .filter(this::isSymbolWinner)
                    .map(it -> new WinningCombination(it.getKey(), this))
                    .toList();
        }

        private boolean isSymbolWinner(Map.Entry<String, Long> entry) {
            return entry.getValue() == count;
        }

        //TODO fix me
        record HorizontallyLinearStrategy(String name, String when,
                                          Double rewardMultiplier) implements WinningStrategy {
            static HorizontallyLinearStrategy horizontallyLinearStrategy(WinningStrategyInputParameter inputParameter) {
                return new HorizontallyLinearStrategy(inputParameter.name(), inputParameter.when(), inputParameter.rewardMultiplier());
            }

            @Override
            public String toString() {
                return name();
            }

            @Override
            public List<WinningCombination> findWinningCombinations(Board board) {
                return new ArrayList<>();
            }
        }
    }
}

