package dev.JustRed23.stonebrick.net;

import dev.JustRed23.stonebrick.exceptions.NetRequestException;
import dev.JustRed23.stonebrick.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class Request {

    private final RequestBuilder builder;
    private boolean cancelled;
    protected boolean cancelCallback;
    private Response response;

    public Request(RequestBuilder builder) {
        this.builder = builder;
    }

    protected Response get() throws NetRequestException {
        if (response != null)
            return response;

        try {
            URL url = URI.create(builder.url).toURL();
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            try {
                c.setRequestMethod(builder.method.name());
                c.setConnectTimeout(builder.connectTO);
                c.setReadTimeout(builder.readTO);
                c.setInstanceFollowRedirects(true);

                if (builder.headers != null && builder.headers.size() > 0) {
                    for (Map.Entry<String, Object> entry : builder.headers.entrySet()) {
                        c.setRequestProperty(entry.getKey(), entry.getValue().toString());
                    }
                }
                c.setDoInput(true);

                check();

                if (builder.body != null) {
                    c.setDoOutput(true);
                    try (OutputStream os = c.getOutputStream()) {
                        os.write(builder.body);
                        os.flush();
                    }
                }

                check();
                byte[] data;
                try (
                        InputStream is = c.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream()
                ) {
                    byte[] buffer = new byte[builder.bufferSize];
                    int read;
                    int total = 0;
                    int available;

                    if (c.getHeaderField("Content-Length") != null)
                        available = Integer.parseInt(c.getHeaderField("Content-Length"));
                    else available = is.available();

                    if (available != 0)
                        NetworkManager.onProgress(this, 0, available);

                    while ((read = is.read(buffer)) != -1) {
                        check();
                        baos.write(buffer, 0, read);
                        total += read;
                        if (available != 0)
                            NetworkManager.onProgress(this, total, available);
                    }

                    if (available != 0)
                        NetworkManager.onProgress(this, 100, 100);

                    data = baos.toByteArray();
                    response = new Response(data, url(), c);
                }
            } catch (Exception e) {
                try (InputStream errorStream = c.getErrorStream()) {
                    response = new Response(StreamUtils.readEntireStream(errorStream), url(), c);
                } catch (IOException e1) {
                    response = new Response(null, url(), c);
                }
            } finally {
                c.disconnect();
            }
        } catch (Exception e) {
            throw new NetRequestException(this, e);
        }
        return response;
    }

    private void check() throws NetRequestException {
        if (cancelled)
            throw new NetRequestException();
    }

    public void cancel() {
        cancel(false);
    }

    public void cancel(boolean force) {
        if (!force && !isCancellable())
            throw new IllegalStateException("Request is not cancellable");
        else cancelled = true;
    }

    public boolean isCancellable() {
        return builder.cancellable;
    }

    public RequestBuilder builder() {
        return builder;
    }

    public String url() {
        return builder.url;
    }

    public Method method() {
        return builder.method;
    }
}