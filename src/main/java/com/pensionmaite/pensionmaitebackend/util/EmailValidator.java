package com.pensionmaite.pensionmaitebackend.util;

import java.util.regex.Pattern;

public class EmailValidator {

    public static boolean IsValidEmail(String email) {

        if (email == null) return false;

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        return pat.matcher(email).matches();
    }
}
