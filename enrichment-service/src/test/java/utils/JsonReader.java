package utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonReader {

    private static final String EMPTY_STRING = "";

    public String readJsonAsString(String fileName) {
        var classLoader = this.getClass().getClassLoader();
        try (var inputStream = classLoader.getResourceAsStream(fileName)) {
            return inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : EMPTY_STRING;
        } catch (IOException e) {
            return EMPTY_STRING;
        }
    }

}
