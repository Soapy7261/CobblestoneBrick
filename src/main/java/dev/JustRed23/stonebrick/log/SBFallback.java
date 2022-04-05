package dev.JustRed23.stonebrick.log;

import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.Util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static dev.JustRed23.stonebrick.log.LogLevel.*;
import static dev.JustRed23.stonebrick.log.FallbackConfig.*;

class SBFallback extends MarkerIgnoringBase {

    private static boolean INITIALIZED = false;

    private static PrintStream TARGET;
    private static DateFormat DATE_FORMATTER;

    private static LogLevel defaultLevel = INFO;

    SBFallback(String name) {
        if (!INITIALIZED)
            init();
        this.name = name;
    }

    static void init() {
        INITIALIZED = true;
        TARGET = getTarget(LOG_LOCATION);
        defaultLevel = DEFAULT_LOG_LEVEL;

        if (DATE_TIME_FORMAT != null) {
            try {
                DATE_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT);
            } catch (IllegalArgumentException e) {
                Util.report("The specified date format is not valid.", e);
                SHOW_DATE_TIME = false;
            }
        } else SHOW_DATE_TIME = false;
    }

    private void write(StringBuilder builder, Throwable t) {
        TARGET.println(builder);
        if (t != null)
            t.printStackTrace(TARGET);
        TARGET.flush();
    }

    public void log(LogLevel level, String message, Throwable t) {
        if (!isLevelEnabled(level))
            return;

        StringBuilder builder = new StringBuilder(32);

        if (SHOW_DATE_TIME)
            builder.append(DATE_FORMATTER.format(new Date())).append(' ');

        if (SHOW_THREAD_NAME)
            builder.append('[')
                    .append(Thread.currentThread().getName())
                    .append(']').append(' ');

        builder.append('[')
                .append(level.name())
                .append(']').append(' ');

        if (SHOW_LOG_NAME)
            builder.append(name).append(" - ");

        builder.append(message);
        write(builder, t);
    }

    public void formatAndLog(LogLevel level, String message, Object... args) {
        if (!isLevelEnabled(level))
            return;
        FormattingTuple ft = MessageFormatter.arrayFormat(message, args);
        log(level, ft.getMessage(), ft.getThrowable());
    }

    //IMPLEMENTED FROM MarkerIgnoringBase
    public void trace(String msg) {
        log(TRACE, msg, null);
    }

    public void trace(String format, Object arg) {
        formatAndLog(TRACE, format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        formatAndLog(TRACE, format, arg1, arg2);
    }

    public void trace(String format, Object... arguments) {
        formatAndLog(TRACE, format, arguments);
    }

    public void trace(String msg, Throwable t) {
        log(TRACE, msg, t);
    }

    public void debug(String msg) {
        log(DEBUG, msg, null);
    }

    public void debug(String format, Object arg) {
        formatAndLog(DEBUG, format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        formatAndLog(DEBUG, format, arg1, arg2);
    }

    public void debug(String format, Object... arguments) {
        formatAndLog(DEBUG, format, arguments);
    }

    public void debug(String msg, Throwable t) {
        log(DEBUG, msg, t);
    }

    public void info(String msg) {
        log(INFO, msg, null);
    }

    public void info(String format, Object arg) {
        formatAndLog(INFO, format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(INFO, format, arg1, arg2);
    }

    public void info(String format, Object... arguments) {
        formatAndLog(INFO, format, arguments);
    }

    public void info(String msg, Throwable t) {
        log(INFO, msg, t);
    }

    public void warn(String msg) {
        log(WARN, msg, null);
    }

    public void warn(String format, Object arg) {
        formatAndLog(WARN, format, arg);
    }

    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(WARN, format, arg1, arg2);
    }

    public void warn(String format, Object... argArray) {
        formatAndLog(WARN, format, argArray);
    }

    public void warn(String msg, Throwable t) {
        log(WARN, msg, t);
    }

    public void error(String msg) {
        log(ERROR, msg, null);
    }

    public void error(String format, Object arg) {
        formatAndLog(ERROR, format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(ERROR, format, arg1, arg2);
    }

    public void error(String format, Object... argArray) {
        formatAndLog(ERROR, format, argArray);
    }

    public void error(String msg, Throwable t) {
        log(ERROR, msg, t);
    }

    //GETTERS
    private static PrintStream getTarget(@NotNull String location) {
        switch (location) {
            case "System.out" -> {
                return System.out;
            }
            case "System.err" -> {
                return System.err;
            }
            default -> {
                try {
                    return new PrintStream(new FileOutputStream(location));
                } catch (FileNotFoundException e) {
                    Util.report("Could not find `" + location + "´. Defaulting to error stream.", e);
                    return System.err;
                }
            }
        }
    }

    private boolean isLevelEnabled(@NotNull LogLevel level) {
        return level.getLevel() >= defaultLevel.getLevel();
    }

    public boolean isTraceEnabled() {
        return isLevelEnabled(TRACE);
    }

    public boolean isDebugEnabled() {
        return isLevelEnabled(DEBUG);
    }

    public boolean isInfoEnabled() {
        return isLevelEnabled(INFO);
    }

    public boolean isWarnEnabled() {
        return isLevelEnabled(WARN);
    }

    public boolean isErrorEnabled() {
        return isLevelEnabled(ERROR);
    }
}