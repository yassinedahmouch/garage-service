package com.renault.garage.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.renault.garage.dto.accessory.AccessoryRequestDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.service.AccessoryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AccessoryController {
    
    private final AccessoryService accessoryService;

    @PostMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<AccessoryResponseDTO> addAccessoryToVehicle(@PathVariable Long vehicleId,
                                                             @RequestBody AccessoryRequestDTO request) {
        
        AccessoryResponseDTO accessoryResponse = accessoryService.addAccessoryToVehicle(vehicleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(accessoryResponse);
    }
    
    @PutMapping("/vehicles/{vehicleId}/accessories/{accessoryId}")
    public ResponseEntity<AccessoryResponseDTO> updateAccessory(@PathVariable Long vehicleId,
                                                                @PathVariable Long accessoryId,
                                                                @RequestBody AccessoryRequestDTO request) {

        return ResponseEntity.ok(
                accessoryService.updateAccessory(vehicleId, accessoryId, request));
    }
    
    @DeleteMapping("/vehicles/{vehicleId}/accessories/{accessoryId}")
    public ResponseEntity<Void> removeAccessory(@PathVariable Long vehicleId,
                                                @PathVariable Long accessoryId) {

        accessoryService.removeAccessoryFromVehicle(vehicleId, accessoryId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/accessories/{accessoryId}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long accessoryId) {

        accessoryService.deleteAccessory(accessoryId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<Set<AccessoryResponseDTO>> listAccessories(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(accessoryService.getAccessoryByVehicule(vehicleId));
    }
    
}
