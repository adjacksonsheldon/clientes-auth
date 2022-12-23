package com.asps.auth.clientesauth.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "grupos_permissoes")
public class GrupoPermissao {
    @EqualsAndHashCode.Include
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @DBRef(lazy = true)
    private Grupo grupo;

    @DBRef(lazy = true)
    private Permissao permissao;
}