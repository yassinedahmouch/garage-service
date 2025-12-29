package com.renault.garage.mapper;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.GarageOpeningTime;
import com.renault.garage.entity.OpeningTime;

@Mapper(
        componentModel = "spring",
        uses = { AddressMapper.class}
    )
    public interface GarageMapper {

    @Mapping(target = "garageId", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingHours", source = "openingHours", qualifiedByName = "mapOpeningHoursToEntity")
    Garage toEntity(GarageRequestDTO dto);

    @Mapping(target = "openingHours", source = "openingHours", qualifiedByName = "mapOpeningHoursToDto")
    GarageResponseDTO toResponseDTO(Garage garage);
    
    OpeningTime toEntity(OpeningTimeDTO dto);
    
    OpeningTimeDTO toResponseDto(OpeningTime entity);

    @Named("mapOpeningHoursToEntity")
    default Set<GarageOpeningTime> mapOpeningHoursToEntity(
            Map<DayOfWeek, List<OpeningTimeDTO>> source) {

        return Optional.ofNullable(source)
            .map(map -> map.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                    .map(dto -> new GarageOpeningTime(
                        entry.getKey(),
                        new OpeningTime(dto.getStartTime(), dto.getEndTime())
                    )))
                .collect(Collectors.toSet()))
            .orElseGet(null);
    }

    @Named("mapOpeningHoursToDto")
    default Map<DayOfWeek, List<OpeningTimeDTO>> mapOpeningHoursToDto(
            Set<GarageOpeningTime> source) {

        return Optional.ofNullable(source)
            .filter(set -> !set.isEmpty())
            .map(set -> {
                Map<DayOfWeek, List<OpeningTimeDTO>> map =
                        new EnumMap<>(DayOfWeek.class);

                set.forEach(got ->
                    map.computeIfAbsent(got.getDayOfWeek(), k -> new ArrayList<>())
                       .add(OpeningTimeDTO.builder()
                           .startTime(got.getOpeningTime().getStartTime())
                           .endTime(got.getOpeningTime().getEndTime())
                           .build())
                );

                return map;
            })
            .orElseGet(null);
    }
    }