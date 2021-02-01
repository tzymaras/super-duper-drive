package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.FrontEndMessages;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (null == statusCode) {
            return "error";
        }

        String errorMsg;
        int errorCode = Integer.parseInt(statusCode.toString());

        switch (errorCode) {
            case 403:
                errorMsg = FrontEndMessages.ERROR_RESPONSE_403;
                break;
            case 404:
                errorMsg = FrontEndMessages.ERROR_RESPONSE_404;
                break;
            default:
                errorMsg = FrontEndMessages.ERROR_RESPONSE_INTERNAL;
                break;
        }

        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("errorCode", errorCode);

        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
