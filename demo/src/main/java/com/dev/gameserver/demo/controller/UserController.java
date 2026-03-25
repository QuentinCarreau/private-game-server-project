package com.dev.gameserver.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.gameserver.demo.model.User;
import com.dev.gameserver.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * CRUD pour les utilisateurs.
 *
 * GET    /api/users        → lister tous les utilisateurs
 * GET    /api/users/{id}   → obtenir un utilisateur par ID
 * PUT    /api/users/{id}   → mettre à jour un utilisateur
 * DELETE /api/users/{id}   → supprimer un utilisateur
 *
 * Note : la création d'utilisateur (POST) passe par l'authentification,
 * pas par cet endpoint (sécurité).
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User updated) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(updated.getUsername());
            existing.setRole(updated.getRole());
            // Note : le mot de passe ne doit PAS être mis à jour ici sans hashage
            return ResponseEntity.ok(userRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
