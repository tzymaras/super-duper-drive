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
        User authenticatedUser = this.userService.getUserByUsername(authentication.getName());
        Note note = new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), authenticatedUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "notes");

        switch (noteForm.getAction()) {
            case "create":
                if (this.noteService.insert(note) == 0) {
                    this.logger.error(String.format("failed creating note: %s", noteForm));
                    redirectAttributes.addFlashAttribute("actionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("actionSuccess", "Note successfully created");
                break;
            case "edit":
                note.setNoteId(noteForm.getNoteId());
                if (this.noteService.update(note) == 0) {
                    this.logger.error(String.format("failed updating note: %s", noteForm));
                    redirectAttributes.addFlashAttribute("actionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("actionSuccess", "Note successfully updated");
                break;
            case "delete":
                if (this.noteService.delete(noteForm.getNoteId(), authenticatedUser.getUserId()) == 0) {
                    this.logger.error(String.format("failed deleting note: %s", noteForm));
                    redirectAttributes.addFlashAttribute("actionError", Constants.ERROR_MSG_INTERNAL_ERROR);
                    return "redirect:home";
                }

                redirectAttributes.addFlashAttribute("actionSuccess", "Note successfully deleted");
                break;
            default:
                redirectAttributes.addFlashAttribute("actionError", "unknown action");
        }

        return "redirect:home";
    }
}