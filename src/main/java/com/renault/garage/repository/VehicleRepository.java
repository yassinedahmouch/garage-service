package com.renault.garage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.renault.garage.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    
    Optional<Vehicle> findByVehicleIdAndGarage_GarageId(Long vehicleId, Long garageId);
}
