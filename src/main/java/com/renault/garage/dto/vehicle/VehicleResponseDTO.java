package com.renault.garage.dto.vehicle;

import java.util.Set;

import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.enums.FuelType;

public record VehicleResponseDTO(Long vehicleId,
        String brand,
        int yearOfManufacture,
        FuelType fuelType,
        GarageResponseDTO garage,
        Set<AccessoryResponseDTO> accessories) {

}
