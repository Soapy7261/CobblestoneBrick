package dev.JustRed23.stonebrick.exeptions;

import dev.JustRed23.stonebrick.net.Request;

public class NetRequestException extends Exception {

    private boolean cancelled;

    public NetRequestException() {
        super("Request cancelled");
        this.cancelled = true;
    }

    public NetRequestException(Request request, Throwable cause) {
        super(String.format("%s %s error: %s", request.method().name(), request.url(), cause.getMessage()), cause);
    }

    public boolean isCancelled() {
        return cancelled;
    }
}