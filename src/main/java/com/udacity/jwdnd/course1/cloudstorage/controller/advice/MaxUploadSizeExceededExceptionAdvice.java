package com.udacity.jwdnd.course1.cloudstorage.controller.advice;

import com.udacity.jwdnd.course1.cloudstorage.constants.FrontEndMessages;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class MaxUploadSizeExceededExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", FrontEndMessages.ERROR_FILES_EXCEEDS_MAX_UPLOAD_LIMIT);
        return "redirect:/home";
    }
}
