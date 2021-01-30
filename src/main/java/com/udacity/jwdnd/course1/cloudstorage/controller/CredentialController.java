package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.Constants;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.credential.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.credential.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String credentialsAction(Authentication authentication, CredentialForm credentialForm, RedirectAttributes redirectAttributes) {
        User authenticatedUser = this.userService.getUserByUsername(authentication.getName());
        Credential credential = this.createCredentialFromForm(credentialForm, authenticatedUser);

        redirectAttributes.addFlashAttribute("activeTab", "credentials");

        switch (credentialForm.getAction()) {
            case "create":
                if (this.credentialService.insert(credential) == 0) {
                    this.logger.error(String.format("failed creating credential: %s", credential));
                    redirectAttributes.addFlashAttribute("actionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("actionSuccess", "Credentials successfully created");
                break;
            case "edit":
                credential.setCredentialId(credentialForm.getCredentialId());
                if (this.credentialService.update(credential) == 0) {
                    this.logger.error(String.format("failed updating credential: %s", credential));
                    redirectAttributes.addFlashAttribute("actionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("actionSuccess", "Credentials successfully updated");
                break;
            case "delete":
                if (this.credentialService.delete(credentialForm.getCredentialId(), authenticatedUser.getUserId()) == 0) {
                    this.logger.error(String.format("failed deleting credential: %s", credential));
                    redirectAttributes.addFlashAttribute("actionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("actionSuccess", "Credentials successfully deleted");
                break;
            default:
                redirectAttributes.addFlashAttribute("actionError", "unknown action");
        }

        return "redirect:home";
    }

    private Credential createCredentialFromForm(CredentialForm form, User user) {
        return new Credential(
                null,
                form.getUrl(),
                form.getUsername(),
                null,
                form.getPassword(),
                user.getUserId()
        );
    }
}
