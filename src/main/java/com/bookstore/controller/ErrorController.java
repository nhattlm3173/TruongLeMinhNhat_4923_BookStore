package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
    @GetMapping("/400")
    public String badRequest() {
        return "error/400";
    }

    @GetMapping("/404")
    public String pageNotFound() {
        return "error/404";
    }
    @GetMapping("/500")
    public String internalServerError() {
        return "error/500";
    }
}
