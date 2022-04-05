package dev.JustRed23.stonebrick.cfg.parsers;

import dev.JustRed23.stonebrick.log.LogLevel;

import java.util.List;

public class LogLevelParser implements IParser<LogLevel> {

    public LogLevel parse(String value) {
        return LogLevel.translate(value);
    }

    public List<Class<?>> parses() {
        return List.of(LogLevel.class);
    }
}