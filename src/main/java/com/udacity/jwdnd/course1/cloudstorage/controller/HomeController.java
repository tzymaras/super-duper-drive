package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.credential.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.note.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String home(Authentication authentication, Model model) {
        User user = this.userService.getUserByUsername(authentication.getName());
        List<Note> userNotes = this.noteService.getAllNotesForUser(user.getUserId());
        List<Credential> userCredentials = this.credentialService.getAllCredentialsForUser(user.getUserId());

        model.addAttribute("userNotes", userNotes);
        model.addAttribute("userCredentials", userCredentials);

        return "home";
    }

    @ModelAttribute("activeTab")
    public String activeTab() {
        return "files";
    }
}
