package dev.JustRed23.stonebrick.cfg.parsers;

import java.util.List;

public class BooleanParser implements IParser<Boolean> {

    public Boolean parse(String value) {
        return Boolean.parseBoolean(value);
    }

    public List<Class<?>> parses() {
        return List.of(boolean.class, Boolean.class);
    }
}