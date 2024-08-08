package com.example.testAi.user.domain.member.controller;

import com.example.testAi.user.domain.member.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe() {
        return "me";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "join";
    }

    /*@PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model)
    {
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        return "login";
    }*/

    @AllArgsConstructor
    public static class MemberJoinForm {
        @NotBlank
        @Size(min = 3)
        private String username;

        @NotBlank
        @Size(min = 4)
        private String password;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 2)
        private String nickname;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid MemberJoinForm form) {
        if (memberService.findByUsername(form.username).isPresent())
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");

        memberService.join(form.username, form.password, form.email, form.nickname, "");

        return "redirect:/member/login";
    }
}