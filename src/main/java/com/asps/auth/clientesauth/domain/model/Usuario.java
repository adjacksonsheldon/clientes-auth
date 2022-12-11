package com.asps.auth.clientesauth.domain.model;

import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "usuarios")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String email;

    @Column
    private String senha;

}
