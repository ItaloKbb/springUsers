package com.utfpr.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "users") // Nome diferente para evitar conflito com palavras reservadas
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;
}
