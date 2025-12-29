package com.renault.garage.dto.vehicle;

import com.renault.garage.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEventDTO {
    private Long vehicleId;
    private String brand;
    private int yearOfManufacture;
    private FuelType fuelType;
    private Long garageId;
}

