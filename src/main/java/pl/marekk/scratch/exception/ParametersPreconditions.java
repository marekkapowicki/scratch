package pl.marekk.scratch.exception;


import static pl.marekk.scratch.exception.Exceptions.illegalArgument;

public final class ParametersPreconditions {
    private ParametersPreconditions() {
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw illegalArgument(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw illegalArgument(errorMessage);
        }
    }
}
