package dev.JustRed23.stonebrick.cfg;

import dev.JustRed23.stonebrick.cfg.parsers.IParser;
import dev.JustRed23.stonebrick.exeptions.ConfigInitializationException;
import dev.JustRed23.stonebrick.util.TripletMap;
import org.reflections.Reflections;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public final class Config {

    private static boolean INITIALIZED = false;

    private static final File configDir = new File(System.getProperty("user.dir") + File.separator + "config");

    private static Map<Class<?>, IParser<?>> parsers;
    private static Map<Class<?>, List<TripletMap<Field, String, Boolean>>> configurations;

    public static void initialize() throws ConfigInitializationException {
        if (INITIALIZED)
            throw new IllegalStateException("Config already initialized");
        INITIALIZED = true;

        System.out.println("=====Initializing Config=====");

        if (!configDir.exists()) {
            boolean created = configDir.mkdir();
            if (!created)
                throw new ConfigInitializationException("Could not create config directory");
        }

        parsers = new HashMap<>();
        mapParsers();

        configurations = new HashMap<>();
        mapConfigurables("dev.JustRed23");
        createConfigurationIfNotExist();

        applyFromFile();

        System.out.println("=====Initialization complete=====");
    }

    private static void mapParsers() {
        System.out.println("==========Adding type parsers==========");
        Reflections reflections = new Reflections(Config.class.getPackageName() + ".parsers");
        reflections.getSubTypesOf(IParser.class).forEach(aClass -> {
            try {
                IParser<?> parser = aClass.getConstructor().newInstance();

                parser.parses().forEach(type -> parsers.put(type, parser));

                System.out.printf("Added parser %s for %s %s%n",
                        parser.getClass().getSimpleName(),
                        parser.parses().size() > 1 ? "types" : "type",
                        parser.parses());

            } catch (Exception e) {
                System.err.println("The parser " + aClass.getSimpleName() + " could not be loaded");
                e.printStackTrace();
            }
        });
    }

    private static void mapConfigurables(String packageName) {
        System.out.println("==========Mapping configurables==========");
        Reflections reflections = new Reflections(packageName);
        System.out.println("Looking for classes annotated with " + Configurable.class.getSimpleName() + " in package '" + packageName + "'");
        reflections.getTypesAnnotatedWith(Configurable.class).forEach(configClass -> {
            System.out.println("\tFound config class: " + configClass.getSimpleName());
            System.out.println("\t\tLooking for fields annotated with " + ConfigField.class.getSimpleName());
            List<TripletMap<Field, String, Boolean>> fieldsWithDefaults = new ArrayList<>();
            Arrays.stream(configClass.getDeclaredFields()).forEach(field -> {
                if (!field.trySetAccessible() || !field.isAnnotationPresent(ConfigField.class))
                    return;

                System.out.println("\t\t\tFound field " + field.getName() + " of type " + field.getType().getSimpleName() + ", setting default value");

                ConfigField configField = field.getAnnotation(ConfigField.class);
                String defaultValue = configField.defaultValue();
                boolean optional = configField.optional();

                try {
                    field.set(field.getType(), parsers.get(field.getType()).parse(defaultValue));
                } catch (Exception e) {
                    System.err.println("\t\t\tCould not set field " + field.getName() + " to it's default value");
                    e.printStackTrace();
                }
                fieldsWithDefaults.add(TripletMap.of(field, defaultValue, optional));
            });
            configurations.put(configClass, fieldsWithDefaults);
        });
    }

    private static void createConfigurationIfNotExist() {
        System.out.println("==========Creating configuration files==========");
        configurations.keySet().forEach(aClass -> {
            String name = aClass.getSimpleName();
            File configFile = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator + name.toLowerCase() + ".cfg");
            String cfgName = configFile.getName();

            System.out.println("File " + cfgName + (configFile.exists() ? " exists, skipping" : " does not exist, creating"));
            if (configFile.exists())
                return;

            try {
                boolean created = configFile.createNewFile();
                if (!created) {
                    System.err.println("\tCould not create file " + cfgName);
                    return;
                }

                PrintWriter pw = new PrintWriter(configFile);
                configurations.get(aClass).forEach(triplet -> {
                    Field field = triplet.first();
                    String defaultValue = triplet.second();

                    pw.println(field.getName() + "=" + defaultValue);
                });
                pw.flush();
                pw.close();
                System.out.println("\tCreated file " + cfgName + " with their default values");
            } catch (IOException e) {
                System.err.println("\tAn error occurred while creating file " + cfgName);
                e.printStackTrace();
            }
        });
    }

    private static void applyFromFile() throws ConfigInitializationException {
        System.out.println("==========Applying configurations from file==========");
        List<ConfigInitializationException> exceptions = new ArrayList<>();
        configurations.keySet().forEach(aClass -> {
            String name = aClass.getSimpleName();
            File configFile = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator + name.toLowerCase() + ".cfg");
            String cfgName = configFile.getName();

            Properties prop = new Properties();
            try (FileInputStream fis = new FileInputStream(configFile)) {
                prop.load(fis);

                configurations.get(aClass).forEach(triplet -> {
                    Field field = triplet.first();
                    String defaultValue = triplet.second();
                    boolean optional = triplet.third();

                    System.out.println("Setting field " + field.getName());
                    String value = prop.getProperty(field.getName());

                    if (value == null) {
                        if (optional) {
                            System.out.println("\tCould not find optional field " + field.getName() + ", setting to default value");
                            value = defaultValue;
                        } else {
                            System.err.println("\tCould not find required field " + field.getName());
                            exceptions.add(new ConfigInitializationException("Could not find required field " + field.getName() + " in file " + cfgName));
                            return;
                        }
                    }

                    if (value.isBlank())
                        value = defaultValue;

                    try {
                        field.set(field.getType(), parsers.get(field.getType()).parse(value));
                    } catch (IllegalAccessException e) {
                        System.err.println("Could not set field " + field.getName() + " to it's value");
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                System.err.println("An error occurred while reading file " + cfgName);
                e.printStackTrace();
            }
        });

        if (!exceptions.isEmpty())
            throw new ConfigInitializationException("Could not initialize configurables, check your terminal for more information", exceptions);
    }
}