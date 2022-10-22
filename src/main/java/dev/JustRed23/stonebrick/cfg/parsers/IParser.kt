package dev.JustRed23.stonebrick.cfg.parsers;

import java.util.List;

public interface IParser<T> {

    T parse(String value);

    List<Class<?>> parses();

    default String toString(Object o) {
        return String.valueOf(o);
    }
}