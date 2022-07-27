package dev.JustRed23.stonebrick.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Args {

    private final Map<String, Object> args = new HashMap<>();

    public void put(String key, Object value) {
        args.put(key, value);
    }

    @Nullable
    public Parseable get(String key) {
        return args.get(key) == null ? null : new Parseable(args.get(key));
    }

    public Map<String, Object> get() {
        return Collections.unmodifiableMap(args);
    }

    //Static methods
    public static Args from(String[] args) {
        return convert(args);
    }

    private static Args convert(String[] args) {
        Args result = new Args();

        if (args.length == 0)
            return result;

        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] split = arg.substring(2).split("=");
                if (split.length == 1) {
                    result.put(split[0], true);
                } else {
                    result.put(split[0], split[1]);
                }
            } else if (arg.startsWith("-")) {
                String[] split = arg.substring(1).split("=");
                if (split.length == 1) {
                    result.put(split[0], true);
                } else {
                    result.put(split[0], split[1]);
                }
            } else {
                result.put(arg, true);
            }
        }
        return result;
    }
}
