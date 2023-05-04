package com.pensionmaite.pensionmaitebackend.util;

import java.util.concurrent.ThreadLocalRandom;

public class ReservationCodeGenerator {

    private static final String CHARACTERS = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ"; // Characters to use for generating reservation code
    public static final int CODE_LENGTH = 6; // Length of reservation code

    public static synchronized String generateReservationCode() {
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            codeBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}

