package com.dev.gameserver.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_servers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private ServerStatus status;
    
    private String ipAddress;
    private int currentPlayers;
    private int maxPlayers;
    
    @ManyToOne
    private Game game;

    @ManyToOne
    private User user;

    public void startSimulation(Game game, int maxPlayers, String ipAddress) {
        this.game = game;
        this.maxPlayers = maxPlayers;
        this.ipAddress = ipAddress;
        this.status = ServerStatus.ONLINE;
        this.currentPlayers = 0;
    }
}
