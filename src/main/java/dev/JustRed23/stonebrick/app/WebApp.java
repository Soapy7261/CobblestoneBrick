package dev.JustRed23.stonebrick.app;

public abstract class WebApp implements IApplication {
    public void init() throws Exception {}
    public abstract void start() throws Exception;
    public abstract void stop() throws Exception;

    public void run() throws Exception {
        //TODO
    }
}
