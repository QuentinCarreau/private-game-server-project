package com.dev.gameserver.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.model.ServerStatus;

@Repository
public interface GameServerRepository extends JpaRepository<GameServer, Long> {
    Optional<GameServer> findByStatus(ServerStatus status);
}
