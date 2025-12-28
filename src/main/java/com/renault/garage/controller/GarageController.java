package com.renault.garage.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.GarageSearchDTO;
import com.renault.garage.entity.Brand;
import com.renault.garage.service.GarageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;
    
    @GetMapping("/{garageId}")
    public ResponseEntity<GarageResponseDTO> getGarageById(@PathVariable Long garageId) {
        GarageResponseDTO garage =  garageService.getGarageById(garageId);
        return ResponseEntity.ok(garage);
    }
    
    
    @PostMapping
    public ResponseEntity<GarageResponseDTO> createGarage(@Valid @RequestBody GarageRequestDTO garageRequestDTO) {
        GarageResponseDTO response = garageService.createGarage(garageRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{garageId}")
    public ResponseEntity<GarageResponseDTO> updateGarage(@PathVariable Long garageId,
                                                          @Valid @RequestBody GarageRequestDTO garageRequestDTO) {
        GarageResponseDTO updatedGarage = garageService.updateGarage(garageId, garageRequestDTO);
        return ResponseEntity.ok(updatedGarage);
    }
    
    @DeleteMapping("/{garageId}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long garageId) {
        garageService.deleteGarage(garageId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public Page<GarageResponseDTO> searchGarages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sortFields,
            @RequestParam(required = false) List<String> sortDirections) {

        GarageSearchDTO criteria = GarageSearchDTO.builder()
                .name(name)
                .city(city)
                .availableBrand(brandId != null ? new Brand() {{ setBrandId(brandId); }} : null)
                .build();

        return garageService.search(criteria, page, size, sortFields, sortDirections);
    }
}
