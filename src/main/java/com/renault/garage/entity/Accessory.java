package com.renault.garage.entity;

import java.util.HashSet;
import java.util.Set;

import com.renault.garage.enums.AccessoryType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessoryId;

    private String name;

    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private AccessoryType accessoryType;
    
    @ManyToMany(mappedBy = "accessories")
    private Set<Vehicle> vehicles = new HashSet<>();
}
