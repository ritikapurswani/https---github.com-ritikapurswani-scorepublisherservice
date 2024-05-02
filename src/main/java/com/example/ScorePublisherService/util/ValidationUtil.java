package com.example.ScorePublisherService.util;

public class ValidationUtil {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isPositiveInteger(long num) {
        return num >= 0;
    }

}
