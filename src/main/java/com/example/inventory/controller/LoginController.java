package com.example.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory.entity.User;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.form.LoginForm;
import com.example.inventory.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login/login";
    }

    @PostMapping
    public String login(@Valid LoginForm form,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginForm", form);
            return "login/login";
        }

        try {
            User user = authService.login(form.getLoginId(), form.getPassword());

            session.setAttribute("loginUserId", user.getUserId());
            session.setAttribute("loginUserName", user.getUserName());
            session.setAttribute("loginUserRole", user.getRole());

            return "redirect:/dashboard";
        } catch (BusinessException e) {
            model.addAttribute("loginForm", form);
            model.addAttribute("errorMessage", e.getMessage());
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}