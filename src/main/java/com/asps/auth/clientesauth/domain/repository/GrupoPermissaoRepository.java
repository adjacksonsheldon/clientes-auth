package com.asps.auth.clientesauth.domain.repository;

import com.asps.auth.clientesauth.domain.model.GrupoPermissao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GrupoPermissaoRepository extends MongoRepository<GrupoPermissao, String> {
    List<GrupoPermissao> findByGrupoId(String id);
}