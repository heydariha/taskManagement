package de.hadi.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class GeneralController {

    @GetMapping({"/", "/home"})
    public String showHomePage(Model model) {
        model.addAttribute("message", "Login");
        model.addAttribute("username", "");
        return "home";
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("message", "Login");
        model.addAttribute("username", "");
        return "login";
    }

}
