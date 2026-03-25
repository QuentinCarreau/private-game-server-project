package com.dev.gameserver.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.gameserver.demo.model.GameTemplate;
import com.dev.gameserver.demo.repository.GameTemplateRepository;

import lombok.RequiredArgsConstructor;

/**
 * CRUD complet pour les templates de jeux.
 * 
 * GET    /api/game-template        → lister tous les templates
 * GET    /api/game-template/{id}   → obtenir un template par ID
 * POST   /api/game-template        → créer un nouveau template
 * PUT    /api/game-template/{id}   → mettre à jour un template
 * DELETE /api/game-template/{id}   → supprimer un template
 */
@RestController
@RequestMapping("/api/game-template")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class GameTemplateController {

    private final GameTemplateRepository gameTemplateRepository;

    @GetMapping
    public List<GameTemplate> getAll() {
        return gameTemplateRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameTemplate> getById(@PathVariable Long id) {
        return gameTemplateRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public GameTemplate create(@RequestBody GameTemplate template) {
        return gameTemplateRepository.save(template);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameTemplate> update(@PathVariable Long id, @RequestBody GameTemplate updated) {
        return gameTemplateRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setInfo(updated.getInfo());
            existing.setScript(updated.getScript());
            existing.setMaxPlayers(updated.getMaxPlayers());
            existing.setCpuUsage(updated.getCpuUsage());
            existing.setRamUsage(updated.getRamUsage());
            existing.setStorageUsage(updated.getStorageUsage());
            return ResponseEntity.ok(gameTemplateRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!gameTemplateRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        gameTemplateRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
