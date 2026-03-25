package com.dev.gameserver.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registred_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name")
    private String username;
    
    @Column(name = "user_password")
    @JsonIgnore // Ne jamais exposer le mot de passe (même hashé) dans les réponses API
    private String password;
    
    @Column(name = "user_role")
    private String role;
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore // Casse la boucle infinie GameServer → User → [GameServer...] → ...
    private List<GameServer> servers;
}
