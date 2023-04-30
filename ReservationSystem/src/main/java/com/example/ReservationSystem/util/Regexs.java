package com.example.ReservationSystem.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexs {

    public static boolean checkMail(String mail){
        String regexForEmail = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern patternOfEmail = Pattern.compile(regexForEmail);

        return patternOfEmail.matcher(mail).matches();
    }

    public static boolean checkPassword(String password){
        String regexForPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

        Pattern patternOfPassword = Pattern.compile(regexForPassword);

        Matcher matcherOfPassword = patternOfPassword.matcher(password);

        return matcherOfPassword.matches();
    }
}
