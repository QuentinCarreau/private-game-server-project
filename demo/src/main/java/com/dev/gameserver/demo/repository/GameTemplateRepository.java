package com.dev.gameserver.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.gameserver.demo.model.GameTemplate;

@Repository
public interface GameTemplateRepository extends JpaRepository<GameTemplate, Long> {
    Optional<GameTemplate> findByName(String name);
}
