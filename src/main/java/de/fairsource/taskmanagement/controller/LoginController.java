package de.fairsource.taskmanagement.controller;

import de.fairsource.taskmanagement.domain.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String loginPageView(Model model) {
        model.addAttribute("message", "Task management Login");
        return "login";
    }

    @PostMapping("login")
    public String loginPageController(Model model) {
        model.addAttribute("message", "Task management Login");
        return "login";
    }

}
