package dev.JustRed23.stonebrick.net;

import dev.JustRed23.stonebrick.exeptions.NetRequestException;

public abstract class Callback {

    public boolean cancellable;

    public abstract void response(Request request, Response response, NetRequestException e);
    public void progress(Request request, int current, int total, int percent) {}
}