package dev.JustRed23.stonebrick.service;

import dev.JustRed23.stonebrick.app.Application;
import dev.JustRed23.stonebrick.log.SBLogger;
import org.slf4j.Logger;

public abstract class Service {

    private long lastRun = 0;
    protected final Logger LOGGER;
    protected Application application;

    public Service() {
        this.LOGGER = SBLogger.getLogger(getClass());
    }

    public abstract boolean shouldRun();
    public abstract long delayBetweenRuns();

    public void init() {}
    public abstract void run() throws Exception;

    public void onComplete() {
        LOGGER.debug("Service " + getName() + " completed");
    }

    public void onError(Exception e) {
        LOGGER.error("Service " + getName() + " failed", e);
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public final void startService() {
        long cur = System.currentTimeMillis();
        long next = lastRun + delayBetweenRuns();

        if (cur >= next) {
            if (!shouldRun())
                return;

            init();

            try {
                run();
            } catch (Exception e) {
                onError(e);
            } finally {
                onComplete();
                lastRun = cur;
            }
        }
    }
}
