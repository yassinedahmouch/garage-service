package com.renault.garage.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long garageId;
    
    private String garageName;
    
    @Embedded
    private Address address;
    
    private String phoneNumber;
    
    private String email;
    
    @ElementCollection
    @CollectionTable(name = "garage_opening_hours", joinColumns = @JoinColumn(name = "garage_id"))
    private Set<GarageOpeningTime> openingHours = new HashSet<>();
    
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new HashSet<>();
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setGarage(this);
    }
}
