package io.github.com.libraryapi.security;

import io.github.com.libraryapi.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomAuthenticationTest {

    @Test
    void getPrincipalDeveRetornarUserDetailsNaoNulo() {
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario.teste");
        usuario.setSenha("senha-criptografada");
        usuario.setRoles(List.of("GERENTE"));

        CustomAuthentication authentication = new CustomAuthentication(usuario);

        Object principal = authentication.getPrincipal();

        assertThat(principal).isNotNull();
        assertThat(principal).isInstanceOf(UserDetails.class);

        UserDetails userDetails = (UserDetails) principal;
        assertThat(userDetails.getUsername()).isEqualTo("usuario.teste");
        assertThat(userDetails.getPassword()).isEqualTo("senha-criptografada");
        assertThat(userDetails.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("GERENTE");
    }
}
