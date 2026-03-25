package com.dev.gameserver.demo.service;

import java.util.List;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.gameserver.demo.exception.ResourceNotFoundException;
import com.dev.gameserver.demo.exception.UnauthorizedAccessException;
import com.dev.gameserver.demo.model.GameTemplate;
import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.model.ResourceUsage;
import com.dev.gameserver.demo.model.ServerStatus;
import com.dev.gameserver.demo.model.User;
import com.dev.gameserver.demo.repository.GameTemplateRepository;
import com.dev.gameserver.demo.repository.GameServerRepository;
import com.dev.gameserver.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServerService {

    private final GameServerRepository serverRepository;
    private final GameTemplateRepository gameTemplateRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public List<GameServer> getAllServers() {
        return serverRepository.findAll();
    }

    public List<GameTemplate> getAllGames() {
        return gameTemplateRepository.findAll();
    }

    @Transactional
    public GameServer startServer(Long serverId, Long gameId, int maxPlayers, String username) {
        GameServer server = getServerAndValidateOwnership(serverId, username);
        GameTemplate template = gameTemplateRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));

        server.setGameTemplate(template);
        server.setMaxPlayers(maxPlayers);
        server.setStatus(ServerStatus.STARTING);
        server.setIpAddress("152.160.42." + (100 + serverId % 150));
        server.setCurrentPlayers(0);

        // Resource usage simulation (still using helper for now, or could map to Host
        // if needed)
        // server.setResourceUsage(new ResourceUsage(5.0, 128.0));

        GameServer saved = serverRepository.save(server);

        // Simuler un délai de démarrage
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                saved.setStatus(ServerStatus.ONLINE);
                serverRepository.save(saved);
                log.info("Server {} is now ONLINE", serverId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return saved;
    }

    @Transactional
    public GameServer stopServer(Long serverId, String username) {
        GameServer server = getServerAndValidateOwnership(serverId, username);
        server.setStatus(ServerStatus.STOPPING);

        GameServer saved = serverRepository.save(server);

        new Thread(() -> {
            try {
                Thread.sleep(1500);
                saved.setStatus(ServerStatus.OFFLINE);
                saved.setCurrentPlayers(0);
                // saved.setResourceUsage(new ResourceUsage(0.0, 0.0));
                serverRepository.save(saved);
                log.info("Server {} is now OFFLINE", serverId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return saved;
    }

    @Transactional
    public GameServer restartServer(Long serverId, String username) {
        GameServer server = getServerAndValidateOwnership(serverId, username);
        server.setStatus(ServerStatus.RESTARTING);
        serverRepository.save(server);

        new Thread(() -> {
            try {
                stopServer(serverId, username);
                Thread.sleep(3000);
                // On récupère les infos nécessaires pour relancer
                startServer(serverId, server.getGameTemplate().getId(), server.getMaxPlayers(), username);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return server;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void simulateResources() {
        List<GameServer> activeServers = serverRepository.findAll().stream()
                .filter(s -> s.getStatus() == ServerStatus.ONLINE)
                .toList();

        for (GameServer server : activeServers) {
            // Randomly update occupied slots
            int players = random.nextInt(server.getMaxPlayers() + 1);
            server.setCurrentPlayers(players);

            serverRepository.save(server);
        }
    }

    private GameServer getServerAndValidateOwnership(Long serverId, String username) {
        GameServer server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Serveur introuvable"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (!server.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Vous n'êtes pas le propriétaire de ce serveur");
        }

        return server;
    }
}
