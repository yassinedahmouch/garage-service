package com.renault.garage.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.renault.garage.dto.vehicle.VehicleRequestDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.dto.vehicle.VehicleSearchDTO;

public interface VehicleService {

    VehicleResponseDTO addVehicleToGarage(Long garageId, VehicleRequestDTO vehicleRequestDTO);
    
    VehicleResponseDTO updateVehicle(Long garageId, Long vehicleId, VehicleRequestDTO vehicleRequestDTO);
    
    void deleteVehicle(Long garageId, Long vehicleId);
    
    Set<VehicleResponseDTO> findVehicleByGarageId(Long garageId);
    
    Page<VehicleResponseDTO> search(VehicleSearchDTO filter);
}
