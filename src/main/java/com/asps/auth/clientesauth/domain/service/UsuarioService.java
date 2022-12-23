package com.asps.auth.clientesauth.domain.service;

import com.asps.auth.clientesauth.domain.model.GrupoPermissao;
import com.asps.auth.clientesauth.domain.repository.GrupoPermissaoRepository;
import com.asps.auth.clientesauth.domain.repository.UsuarioGrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final GrupoPermissaoRepository grupoPermissaoRepository;

    public Set<String> consultarPermissoes(String usuarioId){

        final Set permissoes = new HashSet<>();

        final var grupos = usuarioGrupoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(usuarioGrupo -> usuarioGrupo.getGrupo().getId())
                .collect(Collectors.toSet());

        grupos.stream()
                .map(id -> getGrupoPermissoes(id))
                .collect(Collectors.toSet())
                .forEach(p -> permissoes.addAll(p));

        return permissoes;
    }

    private List<String> getGrupoPermissoes(String id) {
        List<GrupoPermissao> grupoPermissoes = grupoPermissaoRepository.findByGrupoId(id);

        return grupoPermissoes
                .stream()
                .map(grupoPermissao -> grupoPermissao.getPermissao().getNome())
                .collect(Collectors.toList());
    }
}