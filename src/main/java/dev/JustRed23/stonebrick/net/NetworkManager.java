package dev.JustRed23.stonebrick.net;

import dev.JustRed23.stonebrick.exeptions.NetRequestException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    protected static final Object LOCK = new Object();

    private static Map<String, CallbackCollection> requests;

    public static RequestBuilder get(@NotNull String url, @Nullable Object... formatArgs) {
        return new RequestBuilder(processURL(url, formatArgs), Method.GET);
    }

    public static RequestBuilder post(@NotNull String url, @Nullable Object... formatArgs) {
        return new RequestBuilder(processURL(url, formatArgs), Method.POST);
    }

    public static RequestBuilder put(@NotNull String url, @Nullable Object... formatArgs) {
        return new RequestBuilder(processURL(url, formatArgs), Method.PUT);
    }

    public static RequestBuilder delete(@NotNull String url, @Nullable Object... formatArgs) {
        return new RequestBuilder(processURL(url, formatArgs), Method.DELETE);
    }

    public static void cancelAll() {
        cancelAll(false);
    }

    public static void cancelAll(boolean force) {
        new Thread(() -> {
            synchronized (LOCK) {
                if (requests == null)
                    return;

                requests.entrySet().removeIf(entry -> entry.getValue().cancel(force));

                if (requests.size() == 0)
                    requests = null;
            }
        }).start();
    }

    //Helper methods
    private static String processURL(String url, @Nullable Object... args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String str)
                    args[i] = URLEncoder.encode(str, StandardCharsets.UTF_8);
            }
            return String.format(url, args);
        } else return url;
    }

    //Async request handling & progress handling
    protected static boolean addCallback(Callback callback, Request request) {
        synchronized (LOCK) {
            if (requests == null)
                requests = new HashMap<>();

            String key = CallbackCollection.create(request);
            CallbackCollection collection = requests.get(key);

            if (collection != null) {
                collection.addCallback(callback, request);
                return false;
            } else {
                collection = new CallbackCollection();
                collection.addCallback(callback, request);
                requests.put(key, collection);
                return true;
            }
        }
    }

    protected static void onProgress(Request request, int current, int total) {
        synchronized (LOCK) {
            if (requests == null)
                return;

            String key = CallbackCollection.create(request);
            CallbackCollection collection = requests.get(key);

            if (collection != null)
                collection.onProgress(request, current, total);
        }
    }

    protected static void onComplete(Request request, Response response, NetRequestException exception) {
        synchronized (LOCK) {
            if (requests == null)
                return;

            String key = CallbackCollection.create(request);
            CallbackCollection collection = requests.get(key);

            if (collection != null) {
                collection.onComplete(response, exception);
                requests.remove(key);
                if (requests.size() == 0)
                    requests = null;
            }
        }
    }
}