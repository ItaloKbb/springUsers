package com.utfpr.users.security;

import com.utfpr.users.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @SuppressWarnings("unused")
    private final CustomUserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF (para APIs REST)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint) // Resposta customizada para erros de autenticação
                .accessDeniedHandler(accessDeniedHandler) // Resposta customizada para erros de acesso negado
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll() // Permite GET sem autenticação
                .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll() // Permite POST sem autenticação
                .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated() // Exige autenticação para PUT
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated() // Exige autenticação para DELETE
                .anyRequest().permitAll() // Libera qualquer outra requisição (caso queira restringir, altere para `.denyAll()`)
            )
            .httpBasic(withDefaults()); // Habilita autenticação básica
    
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}