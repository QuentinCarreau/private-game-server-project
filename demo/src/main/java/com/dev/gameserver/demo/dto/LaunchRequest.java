package com.dev.gameserver.demo.dto;

import lombok.Data;

@Data
public class LaunchRequest {
    private Long gameId;
    private int maxPlayers;
}