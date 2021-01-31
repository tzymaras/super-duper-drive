package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping
    public String credentialsAction(
            @RequestParam("action") String action,
            Authentication authentication,
            Credential credential,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("activeTab", "credentials");

        User authenticatedUser = this.userService.getUserByUsername(authentication.getName());
        credential.setUserId(authenticatedUser.getUserId());

        switch (action) {
            case "create":
                if (this.credentialService.insert(credential) == 0) {
                    this.logger.error(String.format("failed creating credential: %s", credential));
                    redirectAttributes.addFlashAttribute("errorMessage", "an internal error occured, please try again later");
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("successMessage", "Credentials successfully created");
                break;
            case "edit":
                if (this.credentialService.update(credential) == 0) {
                    this.logger.error(String.format("failed updating credential: %s", credential));
                    redirectAttributes.addFlashAttribute("errorMessage", "an internal error occured, please try again later");
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("successMessage", "Credentials successfully updated");
                break;
            case "delete":
                if (this.credentialService.delete(credential) == 0) {
                    this.logger.error(String.format("failed deleting credential: %s", credential));
                    redirectAttributes.addFlashAttribute("errorMessage", "an internal error occured, please try again later");
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("successMessage", "Credentials successfully deleted");
                break;
            default:
                redirectAttributes.addFlashAttribute("errorMessage", "unknown action");
        }

        return "redirect:home";
    }
}
