package pl.marekk.scratch.common;

public record Range<T extends Comparable<T>>(T fromInclusive, T toExclusive) {
    public static <T extends Comparable<T>> Range<T> between(T fromInclusive, T toExlusive) {
        return new Range<>(fromInclusive, toExlusive);
    }

    public boolean isElementIn(T value) {
        return value.compareTo(fromInclusive) >= 0 && value.compareTo(toExclusive) < 0;
    }
}

