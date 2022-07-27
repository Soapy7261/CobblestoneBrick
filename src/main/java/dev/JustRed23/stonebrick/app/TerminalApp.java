package dev.JustRed23.stonebrick.app;

public abstract class TerminalApp implements IApplication {
    public void init() throws Exception {}
    public abstract void start() throws Exception;
    public void stop() throws Exception {}

    public void run() throws Exception {
        //TODO
    }
}