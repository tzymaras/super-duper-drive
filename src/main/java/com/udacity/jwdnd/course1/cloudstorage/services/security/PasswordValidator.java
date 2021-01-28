package com.udacity.jwdnd.course1.cloudstorage.services.security;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 20;
    private static final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    /**
     * Validates a {@code User} password. Must be longer than {@value PASSWORD_MIN_LENGTH} characters, smaller than
     * {@value PASSWORD_MAX_LENGTH}, have at least one uppercase, one lowercase letter and one symbol
     *
     * @param password the password to be validated
     * @return validation result
     */
    public static boolean isPasswordValid(String password) {
        if (null == password || password.isBlank()) {
            return false;
        }

        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            return false;
        }

        return Pattern.compile(passwordRegex).matcher(password).matches();
    }
}
