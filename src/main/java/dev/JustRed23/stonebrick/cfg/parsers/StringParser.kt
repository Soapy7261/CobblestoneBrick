package dev.JustRed23.stonebrick.cfg.parsers;

import java.util.List;

public class StringParser implements IParser<String> {

    public String parse(String value) {
        return value;
    }

    public List<Class<?>> parses() {
        return List.of(String.class);
    }
}