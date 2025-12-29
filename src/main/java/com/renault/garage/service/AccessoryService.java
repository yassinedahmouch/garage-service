package com.renault.garage.service;

import java.util.Set;

import com.renault.garage.dto.accessory.AccessoryRequestDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;

public interface AccessoryService {

    AccessoryResponseDTO addAccessoryToVehicle(Long vehicleId, AccessoryRequestDTO request);
    
    AccessoryResponseDTO updateAccessory(Long accessoryId, Long vehicleId, AccessoryRequestDTO request);
    
    void removeAccessoryFromVehicle(Long vehicleId, Long accessoryId);
    
    Set<AccessoryResponseDTO> getAccessoryByVehicule(Long vehicleId);
    
    void deleteAccessory(Long accessoryId);
}
