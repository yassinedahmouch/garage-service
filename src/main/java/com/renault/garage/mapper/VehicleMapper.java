package com.renault.garage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.renault.garage.dto.vehicle.VehicleRequestDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Vehicle;

@Mapper(
        componentModel = "spring",
        uses = {AccessoryMapper.class, GarageMapper.class})
public interface VehicleMapper {

    @Mapping(target = "accessories", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "vehicleId", ignore = true)
    @Mapping(target = "brand", ignore = true)
    Vehicle toEntity(VehicleRequestDTO dto);

    @Mapping(target = "vehicleId", source = "vehicleId")
    @Mapping(target = "accessories", source = "accessories")
    @Mapping(target = "garage", source = "garage")
    @Mapping(target = "brand", source = "brand.name")
    VehicleResponseDTO toResponseDto(Vehicle entity);
}
