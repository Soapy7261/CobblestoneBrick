package dev.JustRed23.stonebrick.app;

public abstract class GraphicalApp implements Application {
    public void init() throws Exception {}
    public abstract void start() throws Exception;
    public void stop() throws Exception {}
}
