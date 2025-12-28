package com.renault.garage.service;

import org.springframework.data.domain.Page;

import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.GarageSearchDTO;

public interface GarageService {
    
    GarageResponseDTO getGarageById(Long garageId);
    
    GarageResponseDTO createGarage(GarageRequestDTO garageRequestDTO);
    
    GarageResponseDTO updateGarage(Long garageId, GarageRequestDTO garageRequestDTO);
    
    void deleteGarage(Long garageId);

    Page<GarageResponseDTO> search(GarageSearchDTO criteria);
}
