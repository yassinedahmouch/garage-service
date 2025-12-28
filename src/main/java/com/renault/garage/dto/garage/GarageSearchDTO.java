package com.renault.garage.dto.garage;

import java.util.Map;

import com.renault.garage.entity.Brand;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GarageSearchDTO {

    private String name;
    private String city;
    private Brand availableBrand; 

    private Map<String, String> sort;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10; 
}
