package dev.JustRed23.stonebrick.log;

import org.slf4j.spi.LocationAwareLogger;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum LogLevel {
    TRACE(LocationAwareLogger.TRACE_INT),
    DEBUG(LocationAwareLogger.DEBUG_INT),
    INFO(LocationAwareLogger.INFO_INT),
    WARN(LocationAwareLogger.WARN_INT),
    ERROR(LocationAwareLogger.ERROR_INT);

    private final int level;
    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static LogLevel translate(String raw) throws NoSuchElementException {
        return Arrays.stream(LogLevel.values()).filter(logLevel -> logLevel.name().equalsIgnoreCase(raw)).findFirst().orElseThrow();
    }
}