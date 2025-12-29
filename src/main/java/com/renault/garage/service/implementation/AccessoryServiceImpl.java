package com.renault.garage.service.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renault.garage.dto.accessory.AccessoryRequestDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.BadRequestException;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.AccessoryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccessoryServiceImpl implements AccessoryService {

    private final VehicleRepository vehicleRepository;
    private final AccessoryRepository accessoryRepository;
    private final AccessoryMapper accessoryMapper;
    
    @Override
    @Transactional
    public AccessoryResponseDTO addAccessoryToVehicle(Long vehicleId, AccessoryRequestDTO accessoryRequestDTO) {
        Vehicle vehicle = findVehicle(vehicleId);
        Accessory accessoryToAdd = accessoryMapper.toEntity(accessoryRequestDTO);
        
        vehicle.getAccessories().add(accessoryToAdd);
        accessoryToAdd.getVehicles().add(vehicle);

        accessoryRepository.save(accessoryToAdd);
        
        return accessoryMapper.toResponseDto(accessoryToAdd);
    }

    @Override
    @Transactional
    public AccessoryResponseDTO updateAccessory(Long accessoryId, Long vehicleId, AccessoryRequestDTO accessoryRequestDTO) {
        Vehicle vehicle = findVehicle(vehicleId);

        Accessory accessory = vehicle.getAccessories().stream()
                .filter(a -> a.getAccessoryId().equals(accessoryId))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Accessory not found for this vehicle"));

        accessory.setName(accessoryRequestDTO.name());
        accessory.setDescription(accessoryRequestDTO.description());
        accessory.setPrice(accessoryRequestDTO.price());
        accessory.setAccessoryType(accessoryRequestDTO.accessoryType());

        return accessoryMapper.toResponseDto(accessory);
    }

    @Override
    @Transactional
    public void removeAccessoryFromVehicle(Long vehicleId, Long accessoryId) {
        Vehicle vehicle = findVehicle(vehicleId);

        Accessory accessory = vehicle.getAccessories()
                                    .stream()
                                    .filter(a -> a.getAccessoryId().equals(accessoryId))
                                    .findFirst()
                                    .orElseThrow(() -> new NotFoundException("Accessory not found for this vehicle"));

        vehicle.getAccessories().remove(accessory);
        accessory.getVehicles().remove(vehicle);        
    }

    @Override
    public Set<AccessoryResponseDTO> getAccessoryByVehicule(Long vehicleId) {
        Vehicle vehicle = findVehicle(vehicleId);

        return vehicle.getAccessories().stream()
                                        .map(accessoryMapper::toResponseDto)
                                        .collect(Collectors.toSet());
    }
    
    private Vehicle findVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                                     .orElseThrow(() -> new NotFoundException("Vehicle not found for the given Id"));

        return vehicle;
      }

    @Override
    @Transactional
    public void deleteAccessory(Long accessoryId) {

        Accessory accessory = accessoryRepository.findById(accessoryId)
                                                 .orElseThrow(() -> new NotFoundException("Accessory not found"));

        if (!accessory.getVehicles().isEmpty()) {
            throw new BadRequestException("Accessory is still associated with vehicles","ACCESSORY_IN_USE");
        }

        accessoryRepository.delete(accessory);
    }

}
