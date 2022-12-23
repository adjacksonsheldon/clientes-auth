package com.asps.auth.clientesauth.domain.repository;

import com.asps.auth.clientesauth.domain.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findByEmail(String email);
}
