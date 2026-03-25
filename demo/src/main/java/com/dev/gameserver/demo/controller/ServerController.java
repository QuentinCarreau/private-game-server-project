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
import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.service.GameServerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servers")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ServerController {

    private final GameServerService serverService;

    @GetMapping
    public List<GameServer> getAllServers() {
        return serverService.getAllServers();
    }

    @PostMapping("/{id}/launch")
    public ResponseEntity<GameServer> launchServer(
            @PathVariable Long id,
            @RequestBody LaunchRequest request,
            java.security.Principal principal) {
        
        GameServer server = serverService.startServer(id, request.getGameId(), request.getMaxPlayers(), principal.getName());
        return ResponseEntity.ok(server);
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<GameServer> stopServer(
            @PathVariable Long id,
            java.security.Principal principal) {
        
        GameServer server = serverService.stopServer(id, principal.getName());
        return ResponseEntity.ok(server);
    }

    @PostMapping("/{id}/restart")
    public ResponseEntity<GameServer> restartServer(
            @PathVariable Long id,
            java.security.Principal principal) {
        
        GameServer server = serverService.restartServer(id, principal.getName());
        return ResponseEntity.ok(server);
    }
}
