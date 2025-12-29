package com.renault.garage.dto.vehicle;

import com.renault.garage.enums.FuelType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehicleRequestDTO {

    @NotBlank
    @Size(max = 30)
    private String brand;
    
    @Min(1900)
    @Max(2200)
    private int yearOfManufacture;
    
    @NotNull
    private FuelType fuelType;
}
