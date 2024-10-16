package pl.marekk.scratch.domain;

@FunctionalInterface
public interface GameFactory {
    Game createGame(String filePath);
}
