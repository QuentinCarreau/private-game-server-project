package com.dev.gameserver.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Restreindre les actions administratives aux admins
                .requestMatchers(HttpMethod.POST, "/api/game-server/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/game-server/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/game-server/**").hasRole("ADMIN")
                // Le reste des requêtes API nécessitent d'être au moins authentifié
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
