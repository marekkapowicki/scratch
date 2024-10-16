package pl.marekk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.scratch.api.ScratchCliApi;
import pl.marekk.scratch.api.StartScratchCommand;
import pl.marekk.scratch.exception.Exceptions;

import java.util.Arrays;
import java.util.List;

public class ScratchRunner {
    private static final Logger logger
            = LoggerFactory.getLogger(ScratchRunner.class);

    public static void main(String[] args) {
        final StartScratchCommand startScratchCommand = CommandParser.commandParser(args).tryParse();
        ScratchCliApi.scratchCliApi().play(startScratchCommand);
    }

    private static class CommandParser {
        private static final String CONFIG_PARAM = "--config";
        private static final String BETTING_AMOUNT_PARAM = "--betting-amount";
        private final List<String> inputParams;
        private List<String> MANDATORY_PARAMETERS = List.of(CONFIG_PARAM, BETTING_AMOUNT_PARAM);

        private CommandParser(List<String> inputParams) {
            this.inputParams = inputParams;
        }

        static CommandParser commandParser(String[] args) {
            return new CommandParser(Arrays.stream(args).toList());
        }

        /**
         * ugly parser just to extract params from cli command
         *
         * @return
         */
        StartScratchCommand parseCommand() {
            if (inputParams == null || inputParams.size() != 4) {
                logger.error("wrong input command");
                System.exit(1);
            }
            if (!inputParams.containsAll(MANDATORY_PARAMETERS)) {
                logger.error("missing mandatory input parameter");
                System.exit(1);
            }
            return tryParse();
        }

        StartScratchCommand tryParse() {
            try {
                String configPath = extractFilePath();
                double bettingAmount = extractBettingAmount();
                return StartScratchCommand.startScratchCommand(configPath, bettingAmount);
            } catch (RuntimeException e) {
                logger.error("some error with input command ", e);
                System.exit(1);
            }
            throw Exceptions.illegalState("boom");
        }

        private double extractBettingAmount() {
            final int indexOfAmount = inputParams.indexOf(BETTING_AMOUNT_PARAM) + 1;
            return Double.parseDouble(inputParams.get(indexOfAmount));
        }

        private String extractFilePath() {
            final int indexOfPath = inputParams.indexOf(CONFIG_PARAM) + 1;
            return inputParams.get(indexOfPath);
        }

    }
}