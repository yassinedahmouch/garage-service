package com.renault.garage.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.renault.garage.exception.BadRequestException;

public enum FuelType {

    GASOLINE, DIESEL, ELECTRIC, HYBRID;
    
    @JsonCreator
    public static FuelType from(String value) {
        for (FuelType type : FuelType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BadRequestException("Invalid fuel type: " + value + ". Allowed values: GASOLINE, DIESEL, ELECTRIC, HYBRID",
                "INVALID_FUEL_TYPE");
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
