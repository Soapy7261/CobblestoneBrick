package dev.JustRed23.stonebrick.cfg;

public record Parseable(Object value) {

    public String asString() {
        return String.valueOf(value);
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean(asString());
    }

    public int asInt() {
        return Integer.parseInt(asString());
    }

    public double asDouble() {
        return Double.parseDouble(asString());
    }

    public float asFloat() {
        return Float.parseFloat(asString());
    }
}