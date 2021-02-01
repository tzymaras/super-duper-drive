package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.FrontEndMessages;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String noteAction(@RequestParam("action") String action, Authentication authentication, Note note, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("activeTab", "notes");

        User authenticatedUser = this.userService.getUserByUsername(authentication.getName());
        note.setUserId(authenticatedUser.getUserId());

        switch (action) {
            case "create":
                if (this.noteService.insert(note) == 0) {
                    this.logger.error(String.format("failed creating note: %s", note));
                    redirectAttributes.addFlashAttribute("errorMessage", FrontEndMessages.ERROR_INTERNAL);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("successMessage", FrontEndMessages.SUCCESS_NOTES_CREATE);
                break;
            case "edit":
                if (this.noteService.update(note) == 0) {
                    this.logger.error(String.format("failed updating note: %s", note));
                    redirectAttributes.addFlashAttribute("errorMessage", FrontEndMessages.ERROR_INTERNAL);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("successMessage", FrontEndMessages.SUCCESS_NOTES_EDIT);
                break;
            case "delete":
                if (this.noteService.delete(note) == 0) {
                    this.logger.error(String.format("failed deleting note: %s", note));
                    redirectAttributes.addFlashAttribute("errorMessage", "there was an internal error. please try again later");
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("successMessage", FrontEndMessages.SUCCESS_NOTES_DELETE);
                break;
            default:
                redirectAttributes.addFlashAttribute("errorMessage", FrontEndMessages.ERROR_UNKNOWN_ACTION);
        }

        return "redirect:home";
    }
}