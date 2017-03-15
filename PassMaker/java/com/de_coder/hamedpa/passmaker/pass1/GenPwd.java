package com.de_coder.hamedpa.passmaker.pass1;
//Developed by Hamedpa
import android.app.Activity;
import java.security.SecureRandom;
import java.util.ArrayList;


public class GenPwd {

    int digits;

    boolean upper, lower ,number, specialchar;
    char[] lowercase, uppercase, special, numbers;

    public GenPwd() {
        digits = 6;
        upper = false;
        lower = false;
        number = false;
        specialchar = false;

        lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        special = "?!=$%&/()*+-_.:,;<>@".toCharArray();
        numbers = "0123456789".toCharArray();
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public int getDigits() {
        return digits;
    }

    public void setUpper(boolean upper) {
        this.upper = upper;
    }

    public void setLower(boolean lower) {
        this.lower = lower;
    }

    public void setNumber(boolean number) {
        this.number = number;
    }

    public void setSpecialchar(boolean specialchar) {
        this.specialchar = specialchar;
    }

    public boolean isUpper() {
        return upper;
    }

    public boolean isLower() {
        return lower;
    }

    public boolean isNumber() {
        return number;
    }

    public boolean isSpecialchar() {
        return specialchar;
    }

    /*
     *
     *  GENERATING
     *
     */
    public String generatePassword() {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        ArrayList<String> passes = new ArrayList<String>();
        if (lower == true) {
            for (int i = 0; i < lowercase.length; ++i) {
                passes.add(String.valueOf(lowercase[i]));
            }
        }
        if (upper == true) {
            for (int i = 0; i < uppercase.length; ++i) {
                passes.add(String.valueOf(uppercase[i]));
            }
        }
        if (specialchar == true) {
            for (int i = 0; i < special.length; ++i) {
                passes.add(String.valueOf(special[i]));
            }
        }
        if (number == true) {
            for (int i = 0; i < numbers.length; ++i) {
                passes.add(String.valueOf(numbers[i]));
            }
        }
        for (int i = 0; i < digits; i++) {
            String s = passes.get(random.nextInt(passes.size()));
            sb.append(s);
        }
        String output = sb.toString();
        return output;
    }
}
