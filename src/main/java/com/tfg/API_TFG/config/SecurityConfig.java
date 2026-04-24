package com.tfg.API_TFG.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Login/registro
                        .requestMatchers(HttpMethod.GET, "/cuenta/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cuenta/registro").permitAll()

                        // Endpoints "mi cuenta" (requieren token)
                        .requestMatchers(HttpMethod.PUT, "/cuenta/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/cuenta/me").authenticated()

                        // Públicos (GET)
                        .requestMatchers(HttpMethod.GET,
                                "/alergeno/**", "/compra/**", "/participante/**", "/pelicula/**",
                                "/producto/**", "/sala/**", "/sesion/**", "/usuario/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/sesion/*/*/*/hold-token",
                                "/sesion/*/*/*/butaca/hold"
                        ).permitAll()

                        .requestMatchers(HttpMethod.DELETE,
                                "/sesion/*/*/*/butaca/hold"
                        ).permitAll()

                        // Públicos (POST)
                        .requestMatchers(HttpMethod.POST, "/compra/**", "/cuenta/**", "/usuario/**").permitAll()

                        // Admin
                        .requestMatchers(HttpMethod.POST,
                                "/alergeno/**", "/participante/**", "/pelicula/**",
                                "/producto/**", "/sala/**", "/sesion/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.PUT,
                                "/alergeno/**", "/participante/**", "/pelicula/**",
                                "/producto/**", "/sala/**", "/sesion/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.DELETE,
                                "/alergeno/**", "/participante/**", "/pelicula/**",
                                "/producto/**", "/sala/**", "/sesion/**"
                        ).hasRole("ADMINISTRADOR")

                        // Resto públicos
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter()))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}