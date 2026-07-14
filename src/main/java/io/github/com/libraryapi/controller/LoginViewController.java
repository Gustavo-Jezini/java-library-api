package io.github.com.libraryapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Não é RestController pq não é para ser uma API
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "Login";
    }
}
