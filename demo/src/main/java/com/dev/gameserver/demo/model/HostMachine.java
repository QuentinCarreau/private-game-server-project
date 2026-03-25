package com.dev.gameserver.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "host_machine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "host_machine_name")
    private String name;
    
    @Column(name = "host_machine_status")
    @Enumerated(EnumType.STRING)
    private ServerStatus status; // Re-use common status enum
    
    @Column(name = "host_machine_ip")
    private String ip;
    
    @Column(name = "host_machine_port")
    private int port;
    
    @Column(name = "host_machine_current_game_server_nb")
    private int currentServers;
    
    @Column(name = "host_machine_CPU_max")
    private int cpuMax;
    
    @Column(name = "host_machine_RAM_max")
    private int ramMax;
    
    @Column(name = "host_machine_storage_max")
    private int storageMax;
}
