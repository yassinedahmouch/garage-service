package com.renault.garage.service.implementation;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.GarageSearchDTO;
import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.exception.BadRequestException;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.service.GarageService;
import com.renault.garage.utlis.GarageSpecificationUtil;
import com.renault.garage.utlis.PageableUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GarageServiceImpl implements GarageService{

    private final GarageMapper garageMapper;
    private final GarageRepository garageRepository;
    
    @Override
    @Transactional(readOnly = true)
    public GarageResponseDTO getGarageById(Long garageId) {
        Garage garage = findByGarageId(garageId);
        return garageMapper.toResponseDTO(garage);
    }

    private Garage findByGarageId(Long garageId) {
        return garageRepository.findById(garageId)
                                        .orElseThrow(() -> new NotFoundException("Garage not found for the given Id"));
    }

    @Override
    @Transactional
    public GarageResponseDTO createGarage(GarageRequestDTO garageRequestDTO) {
        validateOpeningHours(garageRequestDTO);
        Garage garage = garageMapper.toEntity(garageRequestDTO);
        garageRepository.save(garage);
        GarageResponseDTO garageResponseDTO = garageMapper.toResponseDTO(garage);
        return garageResponseDTO;
    }

    @Override
    @Transactional
    public GarageResponseDTO updateGarage(Long garageId, GarageRequestDTO garageRequestDTO) {
        validateOpeningHours(garageRequestDTO);
        Garage existingGarage = findByGarageId(garageId);
        Garage updatedGarage = garageMapper.toEntity(garageRequestDTO);
        updatedGarage.setGarageId(existingGarage.getGarageId());
        updatedGarage.setVehicles(existingGarage.getVehicles());
        garageRepository.save(updatedGarage);
        GarageResponseDTO garageResponseDTO = garageMapper.toResponseDTO(updatedGarage);
        return garageResponseDTO;
    }

    @Override
    @Transactional
    public void deleteGarage(Long garageId) {
        garageRepository.delete(findByGarageId(garageId));        
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponseDTO> search(GarageSearchDTO criteria, int page, int size, List<String> sortFields, List<String> sortDirections) {
        Specification<Garage> specification = GarageSpecificationUtil.byCriteria(
                criteria.getName(),
                criteria.getCity(),
                criteria.getAvailableBrand());

        Pageable pageable = PageableUtil.buildPageable(page, size, sortFields, sortDirections);

        return garageRepository.findAll(specification, pageable).map(garageMapper::toResponseDTO);
    }
    
    private void validateOpeningHours(GarageRequestDTO garageRequestDTO) {
        
        Map<DayOfWeek, List<OpeningTimeDTO>> openingHours = garageRequestDTO.getOpeningHours();

        if (openingHours == null || openingHours.isEmpty()) {
            throw new BadRequestException("OPENING_HOURS_EMPTY", "Opening hours must be provided");
        }

        openingHours.forEach((day, slots) -> {

            if (slots == null || slots.isEmpty()) {
                throw new BadRequestException("OPENING_HOURS_EMPTY_DAY", "Opening hours must not be empty for " + day);
            }

            // Validate start < end using stream
            slots.stream().filter(slot -> slot.getStartTime() == null || slot.getEndTime() == null
                    || !slot.getStartTime().isBefore(slot.getEndTime())).findFirst().ifPresent(slot -> {
                        throw new BadRequestException("INVALID_OPENING_TIME_RANGE",
                                "Invalid opening time range for " + day);
                    });

            // Sort once
            List<OpeningTimeDTO> sortedSlots = slots.stream().sorted(Comparator.comparing(OpeningTimeDTO::getStartTime))
                    .toList();

            // Detect overlaps (stream-friendly)
            IntStream.range(1, sortedSlots.size())
                    .filter(i -> !sortedSlots.get(i).getStartTime().isAfter(sortedSlots.get(i - 1).getEndTime()))
                    .findFirst().ifPresent(i -> {
                        throw new BadRequestException("OVERLAPPING_OPENING_HOURS", "Opening hours overlap on " + day);
                    });
        });
    }
}
