package com.renault.garage.dto.vehicle;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehicleSearchDTO {

    private String brand;
    
    private Set<Long> garageId;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    private java.util.Map<String, String> sort;
}
