package com.asps.auth.clientesauth.domain.security.service;

import com.asps.auth.clientesauth.domain.model.Usuario;
import com.asps.auth.clientesauth.domain.repository.UsuarioRepository;
import com.asps.auth.clientesauth.domain.service.UsuarioService;
import com.asps.auth.clientesauth.infrastructure.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JPAUserDetailService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(username);

        final var authorities = usuarioService.consultarPermissoes(usuario.getId())
                .stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao))
                .collect(Collectors.toSet());

        return new AuthUser(usuario, authorities);
    }
}