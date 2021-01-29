package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.Constants;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.note.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.note.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String noteAction(Authentication authentication, NoteForm noteForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("activeTab", "notes");

        User authenticatedUser = this.userService.getUserByUsername(authentication.getName());
        if (!noteForm.getAction().equals("create") && !this.isUserResourceOwner(authenticatedUser, noteForm)) {
            redirectAttributes.addFlashAttribute("noteActionError", "unauthorized action");
            return "redirect:home";
        }

        switch (noteForm.getAction()) {
            case "create":
                Note newNote = new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), authenticatedUser.getUserId());
                if (this.noteService.insert(newNote) == 0) {
                    this.logger.error(String.format("failed creating note: %s", noteForm));
                    redirectAttributes.addFlashAttribute("noteActionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("noteActionSuccess", "Note successfully created");
                break;
            case "edit":
                Note note = new Note(noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), authenticatedUser.getUserId());
                if (this.noteService.update(note) == 0) {
                    this.logger.error(String.format("failed updating note: %s", noteForm));
                    redirectAttributes.addFlashAttribute("noteActionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("noteActionSuccess", "Note successfully updated");
                break;
            case "delete":
                if (this.noteService.delete(noteForm.getNoteId()) == 0) {
                    this.logger.error(String.format("failed deleting note: %s", noteForm));
                    redirectAttributes.addFlashAttribute("noteActionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("noteActionSuccess", "Note successfully deleted");
                break;
            default:
                redirectAttributes.addFlashAttribute("noteActionError", "unknown action");
        }

        return "redirect:home";
    }

    // TODO move to note service or other permission checking class
    private boolean isUserResourceOwner(User user, NoteForm noteForm) {
        return this.noteService.noteExistsForUser(noteForm.getNoteId(), user.getUserId());
    }
}