package com.mindhub.homebanking.utils;

import org.jetbrains.annotations.NotNull;

public final class CardUtils {

    public CardUtils() {
    }
    @NotNull
    public static String getCardNumber() {
        String cardNumber = (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000);
        return cardNumber;
    }

    public static int getCvv() {
        int cvv = (int) ((Math.random() * (999-100)) + 100);
        return cvv;
    }
}
