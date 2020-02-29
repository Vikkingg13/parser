package ru.job4j.parser;

import java.io.*;
import java.util.Properties;

/**
 * Assign file configuration for database.
 * @author Viktor
 * @version 1.0
 */
public class Config {
    private final Properties values = new Properties();
    private String filename;

    public Config(String filename) {
        this.filename = filename;
    }

    /**
     * Initialized configuration.
     */
    public void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(filename)) {
            values.load(in);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Return values by key.
     *
     * @param key for getting value
     * @return values by key.
     */
    public String get(String key) {
        return values.getProperty(key);
    }

    public void set(String key, String value) {
        values.setProperty(key, value);
    }
}
