package com.udacity.jwdnd.course1.cloudstorage.constants;

public interface FrontEndMessages {
    String ERROR_INTERNAL = "an internal error occured, please try again later";
    String ERROR_UNKNOWN_ACTION = "unknown action";

    String ERROR_FILES_EMPTY_FILENAME = "empty file name";
    String ERROR_FILES_EXISTS = "file: %s already exists";

    String ERROR_SIGNUP_USERNAME_EXISTS = "username exists already";
    String ERROR_SIGNUP_INVALID_PASSWORD = "invalid password. your password should contain:\n" +
            "at least %d and at most %d characters,\n" +
            "at least one digit,\n" +
            "at least one upper case letter,\n" +
            "at least one lower case letter,\n" +
            "at least one special character which includes !@#$%%&*()-+=^,\n" +
            "no white space.";

    String SUCCESS_CREDS_CREATE = "Credentials successfully created";
    String SUCCESS_CREDS_EDIT = "Credentials successfully updated";
    String SUCCESS_CREDS_DELETE = "Credentials successfully deleted";

    String SUCCESS_NOTES_CREATE = "Note successfully created";
    String SUCCESS_NOTES_EDIT = "Note successfully updated";
    String SUCCESS_NOTES_DELETE = "Note successfully deleted";

    String SUCCESS_FILES_UPLOAD = "File successfully uploaded";
    String SUCCESS_FILES_DELETE = "File successfully deleted";
}