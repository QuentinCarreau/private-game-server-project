package com.dev.gameserver.demo.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsage {
    private double cpuLoad; // Percentage 0.0 - 100.0
    private double ramUsageMb;
}
