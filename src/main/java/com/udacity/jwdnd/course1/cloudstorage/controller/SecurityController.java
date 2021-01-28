package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.security.HashService;
import com.udacity.jwdnd.course1.cloudstorage.services.security.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(HashService.class);

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(@ModelAttribute User user) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpStore(@ModelAttribute User user, Model model) {
        if (this.userService.userWithUsernameExists(user.getUsername())) {
            model.addAttribute("signupError", "username already exists");
            return "signup";
        }

        if (!this.userService.isPasswordValid(user.getPassword())) {
            model.addAttribute("signupError", this.getInvalidPasswordErrorMessage());
            user.setPassword("");
            return "signup";
        }

        if (this.userService.createUser(user) == 0) {
            this.logger.error(String.format("failed inserting user in db: %s", user));
            model.addAttribute("signupError", "there was an internal error. please try again later");
            return "signup";
        }

        model.addAttribute("signupSuccess", true);

        return "signup";
    }

    private String getInvalidPasswordErrorMessage() {
        return String.format(
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
}
