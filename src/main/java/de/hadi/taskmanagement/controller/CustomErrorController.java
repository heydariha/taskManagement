package de.hadi.taskmanagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public String handleError() {
        return "<body>\n" +
                "  <h1>Error</h1>\n" +
                "  <p>An error occurred while processing your request.</p>\n" +
                "</body>";
    }
}

