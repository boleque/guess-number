package com.live.guessnumber.service.utils;

public class Utils {
    public static int generateGuessNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
