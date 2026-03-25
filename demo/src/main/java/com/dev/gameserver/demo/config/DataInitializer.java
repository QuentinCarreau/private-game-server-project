package com.dev.gameserver.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dev.gameserver.demo.model.User;
import com.dev.gameserver.demo.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("quentin").isEmpty()) {
                User user = new User();
                user.setUsername("quentin");
                user.setPassword(passwordEncoder.encode("password")); // Hash the password
                user.setRole("ADMIN");
                userRepository.save(user);
                System.out.println("Default user 'quentin' created.");
            }
        };
    }
}
