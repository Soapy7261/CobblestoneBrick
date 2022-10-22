package dev.JustRed23.stonebrick.util;

import dev.JustRed23.stonebrick.net.NetworkConfig;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public final class StreamUtils {

    public static void closeQuietly(@Nullable Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable ignored) {
            }
        }
    }

    public static byte[] readEntireStream(@Nullable InputStream is) throws IOException {
        if (is == null)
            return null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[NetworkConfig.BUFFER_SIZE];
            int read;
            while ((read = is.read(buffer)) != -1)
                baos.write(buffer, 0, read);
            baos.flush();
            return baos.toByteArray();
        } finally {
            closeQuietly(is);
        }
    }
}