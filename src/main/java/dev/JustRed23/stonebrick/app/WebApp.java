package dev.JustRed23.stonebrick.app;

public abstract class WebApp implements Application {
    public void init() throws Exception {}
    public abstract void start() throws Exception;
    public abstract void stop() throws Exception;
}
