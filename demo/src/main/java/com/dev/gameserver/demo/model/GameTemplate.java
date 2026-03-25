package com.dev.gameserver.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "game_name")
    private String name;
    
    @Column(name = "game_info")
    private String info;
    
    @Column(name = "game_script")
    private String script;
    
    @Column(name = "game_max_players")
    private int maxPlayers;
    
    @Column(name = "game_CPU_usage")
    private int cpuUsage;
    
    @Column(name = "game_RAM_usage")
    private int ramUsage;
    
    @Column(name = "game_storage_usage")
    private int storageUsage;
}
