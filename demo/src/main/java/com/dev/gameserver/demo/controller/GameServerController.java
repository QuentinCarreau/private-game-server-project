package com.dev.gameserver.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.gameserver.demo.dto.GameRequest;
import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.model.ServerStatus;
import com.dev.gameserver.demo.repository.GameServerRepository;
import com.dev.gameserver.demo.service.GameServerService;

import lombok.RequiredArgsConstructor;

/**
 * CRUD + actions de cycle de vie pour les game servers.
 *
 * GET    /api/game-server            → lister tous les serveurs
 * GET    /api/game-server/{id}       → obtenir un serveur par ID
 * DELETE /api/game-server/{id}       → supprimer un serveur
 *
 * POST   /api/game-server/{id}/launch  → démarrer un serveur
 * POST   /api/game-server/{id}/stop    → arrêter un serveur
 * POST   /api/game-server/{id}/restart → redémarrer un serveur
 */
@RestController
@RequestMapping("/api/game-server")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class GameServerController {

    private final GameServerService serverService;
    private final GameServerRepository serverRepository;

    @GetMapping
    public List<GameServer> getAll() {
        return serverService.getAllServers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameServer> getById(@PathVariable Long id) {
        return serverRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<GameServer> getByStatus(@PathVariable ServerStatus status) {
        return serverRepository.findByStatus(status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!serverRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        serverRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lance un serveur existant (OFFLINE → STARTING → ONLINE).
     * Body : { "gameId": 1, "maxPlayers": 10, "serverName": "Mon Serveur", "privacy": "PUBLIC" }
     */
    @PostMapping("/{id}/launch")
    public ResponseEntity<GameServer> launch(
            @PathVariable Long id,
            @RequestBody GameRequest request,
            java.security.Principal principal) {

        GameServer server = serverService.startServer(
                id, request.getGameId(), request.getMaxPlayers(), principal.getName());
        return ResponseEntity.ok(server);
    }

    /** Arrête un serveur (ONLINE → STOPPING → OFFLINE). */
    @PostMapping("/{id}/stop")
    public ResponseEntity<GameServer> stop(
            @PathVariable Long id,
            java.security.Principal principal) {

        GameServer server = serverService.stopServer(id, principal.getName());
        return ResponseEntity.ok(server);
    }

    /** Redémarre un serveur (ONLINE → RESTARTING → ONLINE). */
    @PostMapping("/{id}/restart")
    public ResponseEntity<GameServer> restart(
            @PathVariable Long id,
            java.security.Principal principal) {

        GameServer server = serverService.restartServer(id, principal.getName());
        return ResponseEntity.ok(server);
    }
}
