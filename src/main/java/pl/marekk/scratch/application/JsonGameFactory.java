package pl.marekk.scratch.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.scratch.domain.Game;
import pl.marekk.scratch.domain.GameFactory;
import pl.marekk.scratch.domain.SymbolsDictionary;
import pl.marekk.scratch.domain.board.Probabilities;
import pl.marekk.scratch.domain.summary.ResultsAnalyzer;

class JsonGameFactory implements GameFactory {
    private static final Logger logger = LoggerFactory.getLogger(JsonGameFactory.class);
    private final JsonConfigurationReader configurationReader;

    private JsonGameFactory(JsonConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    static GameFactory gameFactory() {
        JsonConfigurationReader jsonConfigurationReader = JsonConfigurationReader.configurationReader();
        return new JsonGameFactory(jsonConfigurationReader);
    }

    @Override
    public Game createGame(String filePath) {
        logger.debug("creating the game based on given file {}", filePath);
        final JsonGameConfiguration jsonGameConfiguration = configurationReader.readScratchConfiguration(filePath);
        final SymbolsDictionary symbolsDictionary = jsonGameConfiguration.toSymbolsDictionary();
        final Probabilities probability = jsonGameConfiguration.toProbability();
        final ResultsAnalyzer resultsAnalyzer = jsonGameConfiguration.toResults();

        return Game.createNewGame(symbolsDictionary, probability, resultsAnalyzer, jsonGameConfiguration.columns(), jsonGameConfiguration.rows());
    }
}
