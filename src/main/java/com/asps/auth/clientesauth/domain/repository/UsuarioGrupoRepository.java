package com.asps.auth.clientesauth.domain.repository;

import com.asps.auth.clientesauth.domain.model.UsuarioGrupo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsuarioGrupoRepository extends MongoRepository<UsuarioGrupo, String> {
    List<UsuarioGrupo> findByUsuarioId(String id);
}
