package dev.JustRed23.stonebrick.validation;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public enum Checksum {
    MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256");

    final String algorithm;
    Checksum(String algorithm) {
        this.algorithm = algorithm;
    }

    @NotNull
    public String checksum(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int total;
            while ((total = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, total);
            }
            StringBuilder result = new StringBuilder();
            for (byte b : digest.digest()) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(File file, @NotNull String checksum) {
        return Objects.requireNonNull(checksum).equals(checksum(file));
    }
}
