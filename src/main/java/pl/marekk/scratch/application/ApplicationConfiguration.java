package pl.marekk.scratch.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import pl.marekk.scratch.domain.GameFactory;
import pl.marekk.scratch.domain.GameResultDto;
import pl.marekk.scratch.exception.Exceptions;

public final class ApplicationConfiguration {
    private ApplicationConfiguration() {
    }

    public static GameFactory gameFactory() {
        return JsonGameFactory.gameFactory();
    }

    public static GameResultJsonMapper jsonMapper() {
        return new GameResultJsonMapper();
    }

    public static class GameResultJsonMapper {
        private final ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        public String toJson(GameResultDto resultDto) {
            try {
                return mapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(resultDto);
            } catch (JsonProcessingException e) {
                throw Exceptions.illegalState("error during json converting");
            }
        }
    }
}
