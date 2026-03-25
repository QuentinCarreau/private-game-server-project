package com.dev.gameserver.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gameserver.demo.dto.LaunchRequest;
import com.dev.gameserver.demo.model.Game;
import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.model.User;
import com.dev.gameserver.demo.repository.GameRepository;
import com.dev.gameserver.demo.repository.GameServerRepository;
import com.dev.gameserver.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servers")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ServerController {

    private final GameServerRepository serverRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<GameServer> getAllServers() {
        return serverRepository.findAll();
    }

    @PostMapping("/{id}/launch")
    public ResponseEntity<GameServer> launchServer(
            @PathVariable Long id,
            @RequestBody LaunchRequest request,
            java.security.Principal principal) {

        return serverRepository.findById(id).map(server -> {
            Game game = gameRepository.findById(request.getGameId())
                    .orElseThrow(() -> new RuntimeException("Jeu introuvable"));

            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            server.setUser(user);
            server.startSimulation(game, request.getMaxPlayers(), "152.160.42." + id);
            return ResponseEntity.ok(serverRepository.save(server));
        }).orElse(ResponseEntity.notFound().build());
    }
}
