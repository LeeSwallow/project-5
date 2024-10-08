package com.example.testAi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "start";
    }

    @GetMapping("/todo")
    public String test() {
        return "redirect:/subject/main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/subject/main";
    }

    @GetMapping("/login/success")
    public String success() { return "redirect:/subject/main"; }

    @GetMapping("/member/login/success")
    public String memberSuccess() { return "redirect:/subject/main"; }
}