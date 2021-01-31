package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.slf4j.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        User user = this.userService.getUserByUsername(authentication.getName());

        String fileName = multipartFile.getOriginalFilename();
        if (null == fileName || fileName.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "empty file name");
            return "redirect:/home";
        }

        if (this.fileService.fileExists(fileName, user.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", String.format("file: %s already exists", fileName));
            return "redirect:/home";
        }

        File file = new File(
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                String.valueOf(multipartFile.getSize()),
                multipartFile.getBytes(),
                user.getUserId()
        );

        if (this.fileService.insert(file) == 0) {
            this.logger.error(String.format("failed uploading file:%s", file));
            redirectAttributes.addFlashAttribute("errorMessage", "an internal error occured, try again later");
            return "redirect:/home";
        }

        redirectAttributes.addFlashAttribute("successMessage", "File successfully uploaded");

        return "redirect:/home";
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename, Authentication authentication) {
        User user = this.userService.getUserByUsername(authentication.getName());

        File file = this.fileService.get(filename, user.getUserId());
        if (null == file) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .contentLength(Long.parseLong(file.getFileSize()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String filename, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = this.userService.getUserByUsername(authentication.getName());

        if (this.fileService.delete(filename, user.getUserId()) == 0) {
            this.logger.error(String.format("failed deleting file with id:%s", filename));
            redirectAttributes.addAttribute("errorMessage", "an internal error occurred, please try again later");
            return "redirect:/home";
        }

        redirectAttributes.addFlashAttribute("successMessage", "File successfully deleted");

        return "redirect:/home";
    }
}
