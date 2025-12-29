package com.renault.garage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.exception.BadRequestException;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.service.implementation.GarageServiceImpl;

public class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    private Garage garage;
    private GarageRequestDTO garageRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        garage = new Garage();
        garage.setGarageId(1L);
        garage.setGarageName("Test Garage");

        OpeningTimeDTO openingTime = new OpeningTimeDTO();
        openingTime.setStartTime(LocalTime.of(8, 0));
        openingTime.setEndTime(LocalTime.of(18, 0));

        garageRequestDTO = GarageRequestDTO.builder()
                .garageName("Test Garage")
                .phoneNumber("0123456789")
                .email("test@example.com")
                .address(null)
                .openingHours(Map.of(DayOfWeek.MONDAY, List.of(openingTime)))
                .build();
    }

    @Test
    void testGetGarageById_WhenExists() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageMapper.toResponseDTO(garage)).thenReturn(new GarageResponseDTO(1L, "Test Garage", null, "0123456789", "test@example.com", null));

        GarageResponseDTO response = garageService.getGarageById(1L);

        assertThat(response).isNotNull();
        assertThat(response.garageName()).isEqualTo("Test Garage");
        verify(garageRepository, times(1)).findById(1L);
    }

    @Test
    void testGetGarageById_WhenNotFound() {
        when(garageRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> garageService.getGarageById(2L));
    }

    @Test
    void testCreateGarage_Valid() {
        when(garageMapper.toEntity(garageRequestDTO)).thenReturn(garage);
        when(garageMapper.toResponseDTO(garage)).thenReturn(new GarageResponseDTO(1L, "Test Garage", null, "0123456789", "test@example.com", null));

        GarageResponseDTO response = garageService.createGarage(garageRequestDTO);

        assertThat(response.garageName()).isEqualTo("Test Garage");
        verify(garageRepository, times(1)).save(garage);
    }

    @Test
    void testCreateGarage_InvalidOpeningHours() {
        garageRequestDTO.setOpeningHours(Map.of());

        assertThrows(BadRequestException.class, () -> garageService.createGarage(garageRequestDTO));
    }

    @Test
    void testDeleteGarage() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        garageService.deleteGarage(1L);
        verify(garageRepository, times(1)).delete(garage);
    }
}
