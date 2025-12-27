package com.renault.garage.entity;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, List<OpeningTime>> openingHours = new EnumMap<>(DayOfWeek.class);
    
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new HashSet<>();
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setGarage(this);
    }
}
