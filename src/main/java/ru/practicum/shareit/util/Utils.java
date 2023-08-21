package ru.practicum.shareit.util;

public class Utils {
    public static void checkFromAndSize(int from, int size) {
        if (from < 0 || size < 1) {
            throw new RuntimeException("Incorrect pagination data");
        }
    }
}
