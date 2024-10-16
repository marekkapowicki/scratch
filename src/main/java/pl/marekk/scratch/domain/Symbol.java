package pl.marekk.scratch.domain;

import pl.marekk.scratch.exception.Exceptions;
import pl.marekk.scratch.exception.ParametersPreconditions;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import static pl.marekk.scratch.domain.Symbol.BonusSymbol.bonusSymbol;

public interface Symbol {
    static Symbol newSymbol(String type, InputSymbolParameters inputSymbolParameter) {
        final SymbolTypeFactory symbolTypeFactory = SymbolTypeFactory.fromString(type);
        return symbolTypeFactory.createSymbol(inputSymbolParameter);
    }

    String name();

    enum SymbolTypeFactory {
        STANDARD("standard", input -> new StandardSymbol(input.name, input.rewardMultiplier)),
        BONUS("bonus", input -> bonusSymbol(input.name, input.rewardMultiplier, input.extra, input.impact));
        private final String name;
        private final Function<InputSymbolParameters, Symbol> factoryFunction;


        SymbolTypeFactory(String name, Function<InputSymbolParameters, Symbol> factoryFunction) {
            this.name = name;
            this.factoryFunction = factoryFunction;
        }

        static SymbolTypeFactory fromString(String value) {
            ParametersPreconditions.checkNotNull(value, "symbol can not be null");
            return Arrays.stream(values())
                    .filter(type -> value.equalsIgnoreCase(type.name))
                    .findFirst()
                    .orElseThrow(() -> Exceptions.illegalArgument("wrong symbol type: " + value));
        }

        Symbol createSymbol(InputSymbolParameters inputSymbolParameters) {
            ParametersPreconditions.checkNotNull(inputSymbolParameters, "problem with creation symbols dictionary");
            return factoryFunction.apply(inputSymbolParameters);
        }
    }

    enum BonusImpact {
        miss((a, b) -> 0.0), extra_bonus(Double::sum), multiply_reward((a, b) -> a * b);
        private final BiFunction<Double, Double, Double> bonusCalculator;


        BonusImpact(BiFunction<Double, Double, Double> bonusCalculator) {
            this.bonusCalculator = bonusCalculator;
        }

        BonusImpact fromString(String name) {
            return Arrays.stream(values())
                    .filter(value -> name.equalsIgnoreCase(value.name()))
                    .findFirst()
                    .orElseThrow(() -> Exceptions.illegalState("wrong bonus field: " + name));

        }
    }

    record StandardSymbol(String name, double rewardMultiplier) implements Symbol {
    }

    record BonusSymbol(String name, Double rewardMultiplier, Double extra, BonusImpact impact) implements Symbol {

        static Symbol bonusSymbol(String name, Double rewardMultiplier, Double extra, String impact) {
            return new BonusSymbol(name, rewardMultiplier, extra, BonusImpact.valueOf(impact));
        }

        public Double applyBonus(Double value) {
            return impact.bonusCalculator.apply(chooseNonNullNumber(), value);
        }

        private Double chooseNonNullNumber() {
            return rewardMultiplier != null ? rewardMultiplier : extra;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    record InputSymbolParameters(String name, Double rewardMultiplier, Double extra, String impact) {

    }

}
