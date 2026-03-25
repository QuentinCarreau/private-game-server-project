package com.dev.gameserver.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @Column(name = "game_server_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_server_status")
    private ServerStatus status;

    @Column(name = "ip_adress")
    private String ipAddress;

    private int port;

    @Column(name = "game_server_pwd")
    private String password;

    @Column(name = "saved_world_path")
    private String savedWorldPath;

    @Column(name = "current_players_nb")
    private int currentPlayers;

    @Column(name = "max_players_slot")
    private int maxPlayers;

    @ManyToOne
    @JoinColumn(name = "game_template_id")
    private GameTemplate gameTemplate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
