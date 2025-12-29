package com.renault.garage.mapper;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.renault.garage.dto.accessory.AccessoryRequestDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    @Mapping(target = "accessoryId", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Accessory toEntity(AccessoryRequestDTO dto);

    @Mapping(target = "vehicleIds", source = "vehicles", qualifiedByName = "mapVehiclesToVehicleIds")
    AccessoryResponseDTO toResponseDto(Accessory entity);
    
    @Named("mapVehiclesToVehicleIds")
    default Set<Long> mapVehiclesToVehicleIds(Set<Vehicle> vehicles) {
      return Optional.ofNullable(vehicles).orElseGet(Collections::emptySet).stream()
          .map(Vehicle::getVehicleId).collect(Collectors.toSet());
    }
}
