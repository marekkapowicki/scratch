package pl.marekk.scratch.api;

import java.util.StringJoiner;

import static pl.marekk.scratch.exception.ParametersPreconditions.checkArgument;
import static pl.marekk.scratch.exception.ParametersPreconditions.checkNotNull;

public class StartScratchCommand {
    private final String configPath;
    private final double bettingAmount;

    StartScratchCommand(String configPath, double bettingAmount) {
        this.configPath = configPath;
        this.bettingAmount = bettingAmount;
    }

    public static StartScratchCommand startScratchCommand(String configPath, double bettingAmount) {
        checkNotNull(configPath, "configuration path is null");
        checkArgument(bettingAmount > 0, "betting amount has to be positive");
        return new StartScratchCommand(configPath, bettingAmount);
    }

    String configPath() {
        return configPath;
    }

    double bettingAmount() {
        return bettingAmount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StartScratchCommand.class.getSimpleName() + "[", "]")
                .add("configPath='" + configPath + "'")
                .add("bettingAmount=" + bettingAmount)
                .toString();
    }
}
