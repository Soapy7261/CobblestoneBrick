package dev.JustRed23.stonebrick.app;

public interface Application {
    void init() throws Exception;
    void start() throws Exception;
    void stop() throws Exception;

    //Classes implementing Application directly need to have their own run method.
    //This is because the Application interface is only used to make the application.
    void run() throws Exception;
}