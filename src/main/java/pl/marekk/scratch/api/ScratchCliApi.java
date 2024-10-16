package pl.marekk.scratch.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.scratch.application.ApplicationConfiguration;
import pl.marekk.scratch.domain.Game;
import pl.marekk.scratch.domain.GameFactory;

import static pl.marekk.scratch.exception.ParametersPreconditions.checkNotNull;

public class ScratchCliApi {
    private static final Logger logger
            = LoggerFactory.getLogger(ScratchCliApi.class);
    private final GameFactory gameFactory;
    private final ApplicationConfiguration.GameResultJsonMapper gameResultJsonMapper;

    private ScratchCliApi(GameFactory gameFactory, ApplicationConfiguration.GameResultJsonMapper gameResultJsonMapper) {
        this.gameFactory = gameFactory;
        this.gameResultJsonMapper = gameResultJsonMapper;
    }

    public static ScratchCliApi scratchCliApi() {
        return new ScratchCliApi(ApplicationConfiguration.gameFactory(), ApplicationConfiguration.jsonMapper());
    }

    public void play(StartScratchCommand startScratchCommand) {
        checkNotNull(startScratchCommand, "starting game command is null");
        logger.debug("starting a game for {}", startScratchCommand);
        final Game scratch = gameFactory.createGame(startScratchCommand.configPath());
        logger.info(gameResultJsonMapper.toJson(scratch.playTheGame(startScratchCommand.bettingAmount())));

    }
}
