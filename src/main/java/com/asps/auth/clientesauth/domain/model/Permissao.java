package com.asps.auth.clientesauth.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "permissoes")
public class Permissao {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String nome;

    private String descricao;
}