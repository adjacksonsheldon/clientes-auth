package com.asps.auth.clientesauth.infrastructure.model;

import com.asps.auth.clientesauth.domain.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

    private String usuarioId;
    private String fullName;

    public AuthUser(Usuario usuario, Collection authorities) {
        super(usuario.getEmail(), usuario.getSenha(), authorities);
        this.fullName = usuario.getNome();
        this.usuarioId = usuario.getId();
    }
}
