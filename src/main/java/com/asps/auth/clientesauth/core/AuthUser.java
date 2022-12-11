package com.asps.auth.clientesauth.core;

import com.asps.auth.clientesauth.domain.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class AuthUser extends User {

    private Long usuarioId;
    private String fullName;

    public AuthUser(Usuario usuario) {
        super(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());
        this.fullName = usuario.getNome();
        this.usuarioId = usuario.getId();
    }
}
