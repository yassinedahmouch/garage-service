package com.renault.garage.dto.garage;

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
}
