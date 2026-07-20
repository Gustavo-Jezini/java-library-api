package io.github.com.libraryapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // Não é RestController pq não é para ser uma API
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "Login";
    }

    @GetMapping("/")
    @ResponseBody
    public String paginaHome(Authentication auth) {
        return "Olá " + auth.getName();
    }
}
