package pl.marekk.scratch.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class JsonConfigurationReaderTest {

    private final JsonConfigurationReader configurationReader = JsonConfigurationReader.configurationReader();

    @Test
    void noFile() {
        //expect
        assertThatThrownBy(() -> configurationReader.readScratchConfiguration("/invalidPath/file.yaml"))
                .hasMessageContaining("loading a config file");
    }

    @Test
    void happyPath() {
        //when
        final JsonGameConfiguration gameConfiguration = configurationReader.readScratchConfiguration("src/test/resources/configuration/sample_game_configuration.json");
        //then TODO add more assertons
        assertThat(gameConfiguration).isNotNull();
    }
}