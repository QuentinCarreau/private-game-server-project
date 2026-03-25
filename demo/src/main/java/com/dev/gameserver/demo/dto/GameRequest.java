package com.dev.gameserver.demo.dto;

import lombok.Data;

/**
 * DTO reçu dans le corps (Body) des requêtes HTTP pour les actions sur les serveurs.
 * 
 * Utilisé par : POST /api/servers/{id}/create
 *               POST /api/servers/{id}/launch
 */
@Data
public class GameRequest {

    /**
     * L'ID du template de jeu (Minecraft, Valheim, etc.)
     * Correspond à un enregistrement dans la table games_template.
     */
    private Long gameId;

    /**
     * Le nombre maximum de joueurs autorisés sur le serveur.
     */
    private int maxPlayers;

    /**
     * Le nom personnalisé que l'utilisateur donne à son serveur.
     * Optionnel : si null, un nom par défaut sera généré.
     */
    private String serverName;

    /**
     * La confidentialité du serveur (PUBLIC ou PRIVATE).
     * Valeurs acceptées : "PUBLIC", "PRIVATE"
     */
    private String privacy;

    /**
     * Le mot de passe du serveur (optionnel si PUBLIC, requis si PRIVATE).
     */
    private String password;
}
