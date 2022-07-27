package dev.JustRed23.stonebrick.service;

import dev.JustRed23.stonebrick.app.Application;
import dev.JustRed23.stonebrick.log.SBLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServicePool extends Thread {

    private final Logger LOGGER = SBLogger.getLogger(ServicePool.class);
    private final Application application;
    private final List<Service> services;
    private static boolean init = false, stopRequested = false;

    public ServicePool(Application application) {
        this.application = application;
        services = new ArrayList<>();
    }

    public void addService(Class<? extends Service> serviceClass) {
        try {
            Service service = serviceClass.getConstructor().newInstance();
            service.application = application;
            services.add(service);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        if (init)
            return;
        init = true;
        LOGGER.info("Service pool started");
        while (!stopRequested) {
            services.forEach(Service::startService);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ignored) {} //ignore this and keep running
        }
        LOGGER.info("Shutting down");
    }

    public void shutdown() {
        stopRequested = true;
    }
}
