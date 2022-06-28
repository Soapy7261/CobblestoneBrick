package dev.JustRed23.stonebrick.net;

import dev.JustRed23.stonebrick.exeptions.NetRequestException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    protected String url;
    protected Method method;

    protected Map<String, Object> headers;
    protected byte[] body;

    protected int connectTO;
    protected int readTO;
    protected int bufferSize;

    protected boolean cancellable = true;

    protected RequestBuilder(String url, Method method) {
        this.url = url;
        this.method = method;

        //Default headers
        headers = new HashMap<>();
        header("User-Agent", NetworkConfig.USER_AGENT);
        header("Content-Type", "text/plain");

        //Default IO
        connectTO = NetworkConfig.MAX_CONNECT_TIMEOUT;
        readTO = NetworkConfig.MAX_READ_TIMEOUT;
        bufferSize = NetworkConfig.BUFFER_SIZE;
    }

    //Main methods
    public RequestBuilder header(@NotNull String key, @NotNull Object value) {
        headers.put(key, value);
        return this;
    }

    public RequestBuilder body(byte @Nullable[] body) {
        this.body = body;
        return this;
    }

    public RequestBuilder body(@Nullable String body) {
        if (body == null) {
            this.body = null;
            return this;
        }
        header("Content-Type", "text/plain");
        this.body = body.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public RequestBuilder body(@Nullable JSONObject jsonArray) {
        if (jsonArray == null) {
            this.body = null;
            return this;
        }
        body(jsonArray.toString());
        header("Content-Type", "application/json");
        return this;
    }

    public RequestBuilder body(@Nullable JSONArray jsonArray) {
        if (jsonArray == null) {
            this.body = null;
            return this;
        }
        body(jsonArray.toString());
        header("Content-Type", "application/json");
        return this;
    }

    //Other methods
    public RequestBuilder connectTimeout(@Range(from = 1, to = Integer.MAX_VALUE) int timeout) {
        this.connectTO = timeout;
        return this;
    }

    public RequestBuilder readTimeout(@Range(from = 1, to = Integer.MAX_VALUE) int timeout) {
        this.readTO = timeout;
        return this;
    }

    public RequestBuilder bufferSize(@Range(from = 1, to = Integer.MAX_VALUE) int size) {
        this.bufferSize = size;
        return this;
    }

    public RequestBuilder cancellable(boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    //Build methods
    public Response blocking() throws NetRequestException {
        return new Request(this).get();
    }

    public void async(Callback callback) {
        Request request = new Request(this);
        if (NetworkManager.addCallback(callback, request)) {
            new Thread(() -> {
                try {
                    Response response = request.get();
                    if (request.cancelCallback)
                        return;
                    NetworkManager.onComplete(request, response, null);
                } catch (NetRequestException e) {
                    if (request.cancelCallback)
                        return;
                    NetworkManager.onComplete(request, null, e);
                }
            }).start();
        }
    }
}