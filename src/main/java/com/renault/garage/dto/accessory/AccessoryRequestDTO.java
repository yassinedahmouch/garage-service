package com.renault.garage.dto.accessory;

import com.renault.garage.enums.AccessoryType;

public record AccessoryRequestDTO(String name,
                                  String description,
                                  double price,
                                  AccessoryType accessoryType) {

}
