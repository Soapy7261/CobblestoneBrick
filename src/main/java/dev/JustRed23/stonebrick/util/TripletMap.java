package dev.JustRed23.stonebrick.util;

public record TripletMap<F, S, T>(F first, S second, T third) {

    public static <F, S, T> TripletMap<F, S, T> of(F first, S second, T third) {
        return new TripletMap<>(first, second, third);
    }
}