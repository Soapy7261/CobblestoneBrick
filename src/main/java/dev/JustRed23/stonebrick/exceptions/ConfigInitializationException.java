package dev.JustRed23.stonebrick.exceptions;

import java.util.Collections;
import java.util.List;

public class ConfigInitializationException extends Exception {

    private final List<ConfigInitializationException> causes;

    public ConfigInitializationException(String message) {
        super(message);
        causes = Collections.emptyList();
    }

    public ConfigInitializationException(String message, List<ConfigInitializationException> causes) {
        super(message);
        this.causes = causes;
    }

    public List<ConfigInitializationException> getCauses() {
        return causes;
    }
}