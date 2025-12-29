package com.renault.garage.dto.accessory;

import java.util.Set;

import com.renault.garage.enums.AccessoryType;

public record AccessoryResponseDTO(Long accessoryId,
                                   String name,
                                   String description,
                                   double price,
                                   AccessoryType accessoryType,
                                   Set<Long> vehicleIds
        ) {

}
