package com.asps.auth.clientesauth.core;

import com.asps.auth.clientesauth.domain.model.Usuario;
import com.asps.auth.clientesauth.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JPAUserDetailService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new AuthUser(usuario);
    }
}