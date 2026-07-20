package io.github.com.libraryapi.security;

import io.github.com.libraryapi.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    // Objetivo da CustomAuthentication é padronizar as respostas e formatos de login
    // para que independemente da forma que o login foi feito - Basic, OAuth2, login + password
    // o usuário seja retornado corretamente

    // Ao adicionar o OAuth2 é preciso CustomAuthentication pois ao logar com o google, o retorno
    // do login não é padronizado
    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.usuario
                .getRoles()
                .stream()
                // o Spring adiciona um prefixo em Roles para diferenciar de authority
                //  Por isso, simplesmente adicionar o SimpleGrantedAuthority da forma que está
                // não iria funcionar. Ele iria comparar o atributo role do usuário
                // com o role que ele mesmo configurou com prefixo
                // ROLE_GERENTE != GERENTE
                // .map(role -> new SimpleGrantedAuthority(role))
                // Para resolver temos algumas opções, uma delas é simplesmente add ROLE_ hardcoded
                // .map(role -> new SimpleGrantedAuthority("ROLE_" + role))

                //ou poderiamos apenas configurar um GrantedAuthorityDefaults em SecurityConfiguration
                // para ignorar esse padrão
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .authorities(getAuthorities())
                .build();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
