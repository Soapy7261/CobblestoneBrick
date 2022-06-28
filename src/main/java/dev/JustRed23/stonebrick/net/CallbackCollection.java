package dev.JustRed23.stonebrick.net;

import dev.JustRed23.stonebrick.exeptions.NetRequestException;
import dev.JustRed23.stonebrick.util.CommonThreads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallbackCollection {

    private final Object LOCK = new Object();
    private List<Callback> callbacks;
    private Request first;
    private int percentage = -1;

    public CallbackCollection() {
        callbacks = new ArrayList<>();
    }

    public static String create(Request request) {
        return String.format("%s\0%s\0%s",
                request.method().name(),
                request.url(),
                request.builder().body != null ? request.builder().body.length + "" : ""
        );
    }

    public void addCallback(Callback callback, Request request) {
        synchronized (LOCK) {
            if (callbacks == null)
                throw new IllegalStateException("CallbackCollection is already completed");

            callback.cancellable = request.isCancellable();
            callbacks.add(callback);

            if (first == null)
                first = request;
        }
    }

    public void onComplete(Response response, NetRequestException e) {
        synchronized (LOCK) {
            if (callbacks == null)
                throw new IllegalStateException("CallbackCollection is already completed");

            callbacks.forEach(callback -> CommonThreads.networkThread.execute(() -> callback.response(first, response, e)));
            callbacks.clear();
            callbacks = null;
        }
    }

    public void onProgress(Request request, int current, int total) {
        synchronized (LOCK) {
            if (callbacks == null)
                throw new IllegalStateException("CallbackCollection is already completed");

            int percent = (int) (((double) current / (double) total) * 100);
            if (percent != percentage) {
                percentage = percent;
                callbacks.forEach(callback -> CommonThreads.networkThread.execute(() -> callback.progress(request, current, total, percent)));
            }
        }
    }

    public boolean cancel(boolean force) {
        synchronized (LOCK) {
            if (callbacks == null)
                throw new IllegalStateException("CallbackCollection is already completed");

            Iterator<Callback> iterator = callbacks.iterator();
            while (iterator.hasNext()) {
                final Callback callback = iterator.next();
                if (callback.cancellable || force) {
                    iterator.remove();
                    CommonThreads.networkThread.execute(() -> callback.response(first, null, new NetRequestException()));
                }
            }

            if (callbacks.size() == 0) {
                first.cancelCallback = true;
                first.cancel(force);
                callbacks = null;
                return true;
            } else return false;
        }
    }

    public int size() {
        synchronized (LOCK) {
            return callbacks == null ? -1 : callbacks.size();
        }
    }
}