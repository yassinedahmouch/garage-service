package com.renault.garage.entity;

import java.util.HashSet;
import java.util.Set;

import com.renault.garage.enums.FuelType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;
    
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    
    @Column(name ="year_of_manufacture")
    private int yearOfManufacture;
    
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;
    
    @ManyToMany
    @JoinTable(
        name = "vehicle_accessory",
        joinColumns = @JoinColumn(name = "vehicle_id"),
        inverseJoinColumns = @JoinColumn(name = "accessory_id")
    )
    private Set<Accessory> accessories = new HashSet<>();
    
    
    
}
