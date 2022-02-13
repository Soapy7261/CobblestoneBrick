package dev.JustRed23.stonebrick.log;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.ServiceLoader;

public class SBLogger {

    public static final boolean SLF4J_ENABLED;
    static {
        boolean slf4jEnabled;

        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            slf4jEnabled = true;
        } catch (ClassNotFoundException e) {
            try {
                Class<?> serviceProviderInterface = Class.forName("org.slf4j.spi.SLF4JServiceProvider");
                slf4jEnabled = ServiceLoader.load(serviceProviderInterface).iterator().hasNext();
            } catch (ClassNotFoundException eService) {
                LoggerFactory.getLogger(SBLogger.class);
                slf4jEnabled = false;
            }
        }
        SLF4J_ENABLED = slf4jEnabled;
    }

    private static final Map<String, Logger> LOGGERS = new CaseInsensitiveMap<>();

    private SBLogger() {
        throw new IllegalStateException("Utility class");
    }

    public static Logger getLogger(String name) {
        synchronized (LOGGERS) {
            return SLF4J_ENABLED ? LoggerFactory.getLogger(name)
                    : LOGGERS.computeIfAbsent(name, FallbackLogger::new);
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        synchronized (LOGGERS) {
            return SLF4J_ENABLED ?
                    LoggerFactory.getLogger(clazz)
                    : LOGGERS.computeIfAbsent(clazz.getName(), s -> new FallbackLogger(clazz.getSimpleName()));
        }
    }

    @NotNull
    public static Object lazy(LazyCalculation lazyCalculation) {
        return new Object() {
            public String toString() {
                try {
                    return lazyCalculation.getString();
                } catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    return "Error while evaluating lazy String... " + sw;
                }
            }
        };
    }
}