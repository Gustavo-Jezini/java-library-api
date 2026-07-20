package io.github.com.libraryapi.security;

import io.github.com.libraryapi.model.Usuario;
import io.github.com.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService usuarioService;

    public Usuario obterUsuarioLogado() { // não é possível injetar Authentication diretamente aqui
                                          // pois isso é um metodo não um controller
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // dentro do contexto do security
                                                                                      // existe o getAuthentication para
                                                                                      // conseguir injetar o Authentication
        if (auth instanceof CustomAuthentication customAuthentication) {
            return customAuthentication.getUsuario();
        }

        return null;
    }
}
