package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.security.PasswordValidator;

public interface Constants {
    String ERROR_MSG_SIGNUP_USERNAME_EXISTS = "username exists already";
    String ERROR_MSG_INTERNAL_ERROR = "there was an internal error. please try again later";
    String ERROR_MSG_SIGNUP_INVALID_PASSWORD = String.format(
        "invalid password. your password should contain:\n" +
        "at least %d and at most %d characters,\n" +
        "at least one digit,\n" +
        "at least one upper case letter,\n" +
        "at least one lower case letter,\n" +
        "at least one special character which includes !@#$%%&*()-+=^,\n" +
        "no white space.",
        PasswordValidator.PASSWORD_MIN_LENGTH,
        PasswordValidator.PASSWORD_MAX_LENGTH
    );
}
