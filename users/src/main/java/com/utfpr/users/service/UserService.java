package com.utfpr.users.service;

import com.utfpr.users.dto.UserRequestDTO;
import com.utfpr.users.dto.UserResponseDTO;
import com.utfpr.users.entity.User;
import com.utfpr.users.exception.UserNotFoundException;
import com.utfpr.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean validateBasicAuth(String authHeader) {
        // Verifica se o header começa com "Basic "
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }

        // Extrai e decodifica as credenciais Base64
        String base64Credentials = authHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);

        // Divide em username e password
        String[] values = credentials.split(":", 2);
        if (values.length != 2) {
            return false;
        }

        String username = values[0];
        String password = values[1];

        // Valida as credenciais (substitua pela lógica real)
        return validateCredentialsFromDB(username, password);
    }

    private Boolean validateCredentialsFromDB(String username, String password) {
        // Busca o usuário pelo username no banco de dados
        Optional<User> userOptional = userRepository.findByUsername(username);
    
        // Verifica se o usuário existe e a senha é válida
        return userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword());
    }
    
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail()));
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Criptografa a senha
        user.setEmail(userRequestDTO.getEmail());
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }
}