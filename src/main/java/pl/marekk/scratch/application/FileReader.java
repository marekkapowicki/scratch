package pl.marekk.scratch.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.scratch.exception.Exceptions;

import java.io.File;
import java.io.IOException;

public class FileReader {
    private static final Logger logger
            = LoggerFactory.getLogger(FileReader.class);

    public static byte[] LoadResource(String resourceName) {
        return tryLoadResource(resourceName);
    }

    private static byte[] tryLoadResource(String resourceName) {
        File file = new File(resourceName);
        try {
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            logger.error("config file error ", e);
            throw Exceptions.illegalArgument("error during loading a config file " + e.getMessage());
        }
    }

}
