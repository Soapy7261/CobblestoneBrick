package dev.JustRed23.stonebrick.exeptions;

import dev.JustRed23.stonebrick.net.Response;
import org.jetbrains.annotations.NotNull;

public class NetResponseException extends Exception {

    private Response response;

    public NetResponseException(@NotNull String message) {
        super(message);
    }

    public NetResponseException(@NotNull Response response) {
        super(response.toString());
        this.response = response;
    }

    public NetResponseException(@NotNull Response response, @NotNull Exception e) {
        super(String.format("%s: %s", response, e.getLocalizedMessage()), e);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}