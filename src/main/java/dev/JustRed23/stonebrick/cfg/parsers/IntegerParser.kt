package dev.JustRed23.stonebrick.cfg.parsers;

import java.util.List;

public class IntegerParser implements IParser<Integer> {

    public Integer parse(String value) {
        return Integer.parseInt(value);
    }

    public List<Class<?>> parses() {
        return List.of(int.class, Integer.class);
    }
}