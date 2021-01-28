package com.udacity.jwdnd.course1.cloudstorage.services.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    @Test
    void isPasswordValid() {
        Assertions.assertFalse(PasswordValidator.isPasswordValid("passS1@"));
        Assertions.assertFalse(PasswordValidator.isPasswordValid("SADFWELksdd@"));
        Assertions.assertFalse(PasswordValidator.isPasswordValid("SADFWELksdd2"));
        Assertions.assertFalse(PasswordValidator.isPasswordValid("WAYtoolongpassword22@"));

        Assertions.assertTrue(PasswordValidator.isPasswordValid("4584456Aa@#"));
    }
}