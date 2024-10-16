package pl.marekk.scratch.exception;

public class Exceptions {
    private Exceptions() {
    }

    public static RuntimeException illegalArgument(String message) {
        return new IllegalArgumentException(message);
    }

    public static RuntimeException illegalState(String message) {
        return new IllegalStateException(message);
    }
}
