package com.dev.gameserver.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gameserver.demo.model.GameTemplate;
import com.dev.gameserver.demo.repository.GameTemplateRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class GameController {

    private final GameTemplateRepository gameTemplateRepository;

    @GetMapping
    public List<GameTemplate> getAllGames() {
        return gameTemplateRepository.findAll();
    }
}
