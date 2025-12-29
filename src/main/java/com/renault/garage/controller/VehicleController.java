package com.renault.garage.controller;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.renault.garage.dto.vehicle.VehicleRequestDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.dto.vehicle.VehicleSearchDTO;
import com.renault.garage.service.VehicleService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class VehicleController {
    
    private final VehicleService vehicleService;

    
    @PostMapping("/garages/{garageId}/vehicles")
    ResponseEntity<VehicleResponseDTO> addVehicleToGarage(@PathVariable("garageId") Long garageId,
                                                          @Valid @RequestBody VehicleRequestDTO vehicleRequest) {
        VehicleResponseDTO vehicleResponseDTO = vehicleService.addVehicleToGarage(garageId, vehicleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleResponseDTO);
    }
    
    @PutMapping("/garages/{garageId}/vehicles/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Long garageId,
                                                            @PathVariable Long vehicleId,
                                                            @RequestBody @Valid VehicleRequestDTO vehicleRequestDTO) {

        VehicleResponseDTO vehicleResponseDTO = vehicleService.updateVehicle(garageId, vehicleId, vehicleRequestDTO);
        return ResponseEntity.ok(vehicleResponseDTO);
    }
    
    @DeleteMapping("/garages/{garageId}/vehicles/{vehicleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable Long garageId,
                              @PathVariable Long vehicleId) {

        vehicleService.deleteVehicle(garageId, vehicleId);
    }
    
    @GetMapping("/garages/{garageId}/vehicles")
    public ResponseEntity<Set<VehicleResponseDTO>> getVehiclesByGarage(@PathVariable Long garageId) {

        Set<VehicleResponseDTO> page = vehicleService.findVehicleByGarageId(garageId);
        return ResponseEntity.ok(page);
    }
    
    @PostMapping("/search")
    public ResponseEntity<Page<VehicleResponseDTO>> searchVehicles(
            @RequestBody VehicleSearchDTO criteria) {

        Page<VehicleResponseDTO> page = vehicleService.search(criteria);

        return ResponseEntity.ok(page);
    }
}
