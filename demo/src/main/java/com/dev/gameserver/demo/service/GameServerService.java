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
import com.dev.gameserver.demo.model.ServerPrivacy;
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

    public GameServer createServer(Long gameId, int maxPlayers, String username,
                                   String serverName, String privacy, String password) {
        GameTemplate template = gameTemplateRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        GameServer server = new GameServer();
        server.setGameTemplate(template);
        server.setMaxPlayers(maxPlayers);
        server.setStatus(ServerStatus.OFFLINE);
        server.setCurrentPlayers(0);
        server.setUser(user);
        server.setName(serverName != null ? serverName : template.getName() + " - Serveur");
        server.setPrivacy(privacy != null ? ServerPrivacy.valueOf(privacy) : ServerPrivacy.PUBLIC);
        server.setPassword(password);
        // Simule le chemin de sauvegarde (à remplacer par le vrai path plus tard)
        server.setSavedWorldPath("/saves/" + username + "/" + template.getName().toLowerCase().replace(" ", "_"));
        server.setPort(25565);
        return serverRepository.save(server);
    }

    /**
     * Lance un serveur en 4 étapes :
     * 1. Vérification des ressources CPU/RAM/Storage disponibles
     * 2. Simulation de l'exécution du script de lancement
     * 3. Attribution de l'adresse IP
     * 4. Passage en statut ONLINE
     */
    @Transactional
    public GameServer startServer(Long serverId, String username) {
        GameServer server = getServerAndValidateOwnership(serverId, username);
        GameTemplate template = server.getGameTemplate();

        // === ÉTAPE 1 : Vérification des ressources ===
        // Simule la vérification de disponibilité des ressources de la machine hôte
        log.info("[Step 1] Checking resources for server {} (CPU:{}, RAM:{}, Storage:{})",
                serverId, template.getCpuUsage(), template.getRamUsage(), template.getStorageUsage());
        // TODO: remplacer par une vraie vérification des ressources de HostMachine
        boolean resourcesAvailable = true; // simulation
        if (!resourcesAvailable) {
            throw new IllegalStateException("Ressources insuffisantes pour lancer ce serveur.");
        }

        // === ÉTAPE 2 : Exécution du script de lancement ===
        log.info("[Step 2] Simulating script execution: {}", template.getScript());
        // TODO: remplacer par ProcessBuilder pour exécuter le vrai script
        boolean scriptSuccess = true; // simulation
        if (!scriptSuccess) {
            throw new IllegalStateException("Échec de l'exécution du script de lancement.");
        }

        // === ÉTAPE 3 : Attribution de l'adresse IP ===
        String simulatedIp = "152.160.42." + (100 + serverId % 150);
        log.info("[Step 3] IP assigned: {}", simulatedIp);
        server.setIpAddress(simulatedIp);
        server.setStatus(ServerStatus.STARTING);
        server.setCurrentPlayers(0);
        GameServer saved = serverRepository.save(server);

        // === ÉTAPE 4 : Passage ONLINE (après délai simulé) ===
        new Thread(() -> {
            try {
                Thread.sleep(2000); // simule le temps de boot
                saved.setStatus(ServerStatus.ONLINE);
                serverRepository.save(saved);
                log.info("[Step 4] Server {} is now ONLINE at {}", serverId, simulatedIp);
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
                startServer(serverId, username);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return server;
    }

    @Transactional
    public GameServer updateServer(Long serverId, String serverName, int maxPlayers, String privacy, String password, String username) {
        GameServer server = getServerAndValidateOwnership(serverId, username);
        if (serverName != null) server.setName(serverName);
        if (maxPlayers > 0) server.setMaxPlayers(maxPlayers);
        if (privacy != null) server.setPrivacy(ServerPrivacy.valueOf(privacy));
        if (password != null) server.setPassword(password.isEmpty() ? null : password);
        return serverRepository.save(server);
    }

    @Transactional
    public void deleteServer(Long serverId, String username) {
        GameServer server = getServerAndValidateOwnership(serverId, username);
        serverRepository.delete(server);
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void simulateResources() {
        List<GameServer> activeServers = serverRepository.findAll().stream()
                .filter(s -> s.getStatus() == ServerStatus.ONLINE)
                .toList();

        for (GameServer server : activeServers) {
            GameTemplate template = server.getGameTemplate();

            // Randomly update occupied slots
            int players = random.nextInt(server.getMaxPlayers() + 1);
            server.setCurrentPlayers(players);

            // Simulate CPU utilization (10% to 90% of allocated limit)
            double maxCpu = template.getCpuUsage();
            server.setCurrentCpuUsage(Math.round((maxCpu * (0.1 + random.nextDouble() * 0.8)) * 10.0) / 10.0);

            // Simulate RAM utilization (30% to 95% of allocated limit)
            double maxRam = template.getRamUsage();
            server.setCurrentRamUsage(Math.round((maxRam * (0.3 + random.nextDouble() * 0.65)) * 10.0) / 10.0);

            // Simulate Storage utilization (50% to 100% of allocated limit)
            double maxStorage = template.getStorageUsage();
            server.setCurrentDiskUsage(Math.round((maxStorage * (0.5 + random.nextDouble() * 0.5)) * 10.0) / 10.0);

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
