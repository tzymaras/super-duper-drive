package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.Constants;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.security.HashService;
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
    private final Logger logger = LoggerFactory.getLogger(SecurityController.class);

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
            model.addAttribute("signupError", Constants.ERROR_MSG_SIGNUP_USERNAME_EXISTS);
            return "signup";
        }

        if (!this.userService.isPasswordValid(user.getPassword())) {
            model.addAttribute("signupError", Constants.ERROR_MSG_SIGNUP_INVALID_PASSWORD);
            user.setPassword("");
            return "signup";
        }

        if (this.userService.createUser(user) == 0) {
            this.logger.error(String.format("failed adding user: %s", user));
            model.addAttribute("signupError", Constants.ERROR_MSG_INTERNAL_ERROR);
            return "signup";
        }

        model.addAttribute("signupSuccess", true);

        return "signup";
    }
}
