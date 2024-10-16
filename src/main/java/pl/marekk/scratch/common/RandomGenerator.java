package pl.marekk.scratch.common;


import pl.marekk.scratch.exception.Exceptions;

import java.util.Random;

public final class RandomGenerator {
    private static final Random randoms = new Random();

    private RandomGenerator() {

    }

    public static int randomNumberInRange(int minValue, int maxValue) {
        return minValue + randoms.nextInt((maxValue - minValue));
    }

    public static int randomNumberInRange(Range<Integer> range) {
        final int randomNumber = randomNumberInRange(range.fromInclusive(), range.toExclusive());
        if (!range.isElementIn(randomNumber)) {
            throw Exceptions.illegalState("exceptions during generating symbol in cell");
        }
        return randomNumber;
    }
}
