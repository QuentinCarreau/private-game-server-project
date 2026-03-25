package com.dev.gameserver.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.model.ServerStatus;

@Repository
public interface GameServerRepository extends JpaRepository<GameServer, Long> {

    // Trouver un serveur par son statut
    List<GameServer> findByStatus(ServerStatus status);

    // Trouver tous les serveurs d'un utilisateur
    List<GameServer> findByUserId(Long userId);

    // Trouver tous les serveurs utilisant un template de jeu donné
    List<GameServer> findByGameTemplateId(Long gameTemplateId);

    // Trouver un serveur spécifique appartenant à un utilisateur avec un statut donné
    Optional<GameServer> findByIdAndUserId(Long id, Long userId);

    // Vérifier si un serveur existe pour un utilisateur donné
    boolean existsByIdAndUserId(Long id, Long userId);
}
