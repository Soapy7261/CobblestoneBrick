package dev.JustRed23.stonebrick.net;

import dev.JustRed23.stonebrick.exceptions.NetResponseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Response {

    private final String url;
    private final byte[] data;
    private final int responseCode;
    private final String responseMessage;
    private final Map<String, List<String>> headers;

    protected Response(byte[] data, String url, HttpURLConnection connection) throws IOException {
        this.data = data;
        this.url = url;
        this.responseCode = connection.getResponseCode();
        this.responseMessage = connection.getResponseMessage();
        this.headers = connection.getHeaderFields();
    }

    public String url() {
        return url;
    }

    public int responseCode() {
        return responseCode;
    }

    public String responseMessage() {
        return responseMessage;
    }

    public String header(String name) {
        return headers.get(name).get(0);
    }

    public List<String> headerList(String name) {
        return headers.get(name);
    }

    public int contentLength() {
        String contentLength = header("Content-Length");
        if (contentLength == null)
            return -1;

        return Integer.parseInt(contentLength);
    }

    public String contentType() {
        return header("Content-Type");
    }

    public boolean isSuccess() {
        return responseCode == HttpURLConnection.HTTP_OK;
    }

    public Response throwIfNotSuccess() throws NetResponseException {
        if (!isSuccess())
            throw new NetResponseException(this);
        return this;
    }

    public byte[] asBytes() {
        return data;
    }

    public String asString() {
        if (data == null || data.length == 0)
            return null;
        return new String(data, StandardCharsets.UTF_8);
    }

    public JSONObject asJSONObject() {
        String json = asString();
        return json == null ? new JSONObject() : new JSONObject(json);
    }

    public JSONArray asJSONArray() {
        String json = asString();
        return json == null ? new JSONArray() : new JSONArray(json);
    }

    public void asFile(File destination) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(destination)) {
            fos.write(data);
            fos.flush();
        }
    }

    public String toString() {
        return String.format("%s, %d %s, %d bytes", url, responseCode, responseMessage, data != null ? data.length : 0);
    }
}