package com.utfpr.users.repository;

import com.utfpr.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Busca um usuário pelo username
    Optional<User> findByUsername(String username);

    // Verifica se um username já existe
    boolean existsByUsername(String username);

    // Verifica se um email já existe
    boolean existsByEmail(String email);

    // Busca um usuário pelo email
    Optional<User> findByEmail(String email);

    // Busca usuários cujo username contém uma string
    List<User> findByUsernameContaining(String username);

    // Busca usuários com emails que terminam com um domínio específico
    List<User> findByEmailEndingWith(String domain);

    // Consulta personalizada: busca um usuário por username e email
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.email = :email")
    Optional<User> findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

    // Retorna todos os usuários paginados
    Page<User> findAll(Pageable pageable);

    // Retorna usuários filtrados por username e paginados
    Page<User> findByUsernameContaining(String username, Pageable pageable);
}