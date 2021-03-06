package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;
    private final UserService userService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    public String home(Authentication authentication, Model model) {
        User user = this.userService.getUserByUsername(authentication.getName());
        
        List<Note> userNotes = this.noteService.getAllNotesForUser(user.getUserId());
        List<Credential> userCredentials = this.credentialService.getAllCredentialsForUser(user.getUserId());
        List<File> userFiles = this.fileService.getAllUserFiles(user.getUserId());

        model.addAttribute("userNotes", userNotes);
        model.addAttribute("userCredentials", userCredentials);
        model.addAttribute("userFiles", userFiles);

        return "home";
    }

    @ModelAttribute("activeTab")
    public String activeTab() {
        return "files";
    }
}
