package pl.marekk.scratch.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.scratch.exception.Exceptions;

import java.io.IOException;

import static pl.marekk.scratch.application.FileReader.LoadResource;

class JsonConfigurationReader {
    private static final Logger logger = LoggerFactory.getLogger(JsonConfigurationReader.class);
    private final ObjectMapper jsonMapper;

    private JsonConfigurationReader(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    static JsonConfigurationReader configurationReader() {
        return new JsonConfigurationReader(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE));
    }

    public JsonGameConfiguration readScratchConfiguration(String pathToConfigFile) {
        if (pathToConfigFile == null) {
            throw Exceptions.illegalArgument("empty path to configuration file");
        }
        try {
            logger.debug("reading configuration file {}", pathToConfigFile);
            return jsonMapper.readValue(LoadResource(pathToConfigFile), JsonGameConfiguration.class);
        } catch (IOException e) {
            logger.error("exception during reading configuration class", e);
            throw Exceptions.illegalArgument(e.getMessage());
        }
    }
}
