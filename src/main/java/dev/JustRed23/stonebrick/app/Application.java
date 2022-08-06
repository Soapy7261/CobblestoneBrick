package dev.JustRed23.stonebrick.app;

import dev.JustRed23.stonebrick.service.ServicePool;
import dev.JustRed23.stonebrick.util.Args;
import dev.JustRed23.stonebrick.util.CommonThreads;

import java.util.concurrent.CountDownLatch;

public abstract class Application {

    private Args args;
    private final ServicePool servicePool = new ServicePool(this);

    protected abstract void init() throws Exception;
    protected abstract void start() throws Exception;
    protected abstract void stop() throws Exception;

    //The heart of the application. Make sure to call this method in your run method.
    public static void launch(Class<? extends Application> appClass, String[] args) {
        Launcher.launchApplication(appClass, args);
    }

    public static void launch(String[] args) {
        StackTraceElement[] tree = Thread.currentThread().getStackTrace();
        boolean found = false;
        String callingClassName = null;

        for (StackTraceElement se : tree) {
            String className = se.getClassName();
            String methodName = se.getMethodName();
            if (found) {
                callingClassName = className;
                break;
            } else if (Application.class.getName().equals(className) && "launch".equals(methodName))
                found = true;
        }

        if (callingClassName == null)
            throw new RuntimeException("Error: unable to determine Application class");

        try {
            Class theClass = Class.forName(callingClassName, false, Thread.currentThread().getContextClassLoader());
            if (Application.class.isAssignableFrom(theClass))
                launch((Class<? extends Application>) theClass, args);
            else
                throw new RuntimeException("Error: " + theClass + " does not extend " + Application.class.getCanonicalName());
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void exit() {
        Launcher.stop();
    }

    //Runnables
    private static final Object runLock = new Object();
    public static void runAndWait(Runnable runnable) {
        final CountDownLatch doneLatch = new CountDownLatch(1);
        runLater(() -> {
            try {
                runnable.run();
            } finally {
                doneLatch.countDown();
            }
        });

        try {
            doneLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runLater(Runnable runnable) {
        synchronized (runLock) {
            CommonThreads.appThread.submit(runnable::run);
        }
    }

    //GETTERS
    void setArgs(Args args) {
        this.args = args;
    }

    public Args getArgs() {
        return args;
    }

    public ServicePool getServicePool() {
        return servicePool;
    }
}