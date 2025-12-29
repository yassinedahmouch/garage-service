package com.renault.garage.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;

import com.renault.garage.dto.vehicle.VehicleRequestDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Brand;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.messaging.VehiclePublisher;
import com.renault.garage.repository.BrandRepository;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.implementation.VehicleServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private VehiclePublisher vehiclePublisher;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Garage garage;
    private Vehicle vehicle;
    private VehicleRequestDTO vehicleRequestDTO;
    private Brand brand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        garage = new Garage();
        garage.setGarageId(1L);

        brand = new Brand();
        brand.setName("Renault");

        vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setGarage(garage);
        vehicle.setBrand(brand);
        vehicle.setFuelType(FuelType.GASOLINE);
        vehicle.setYearOfManufacture(2020);

        vehicleRequestDTO = VehicleRequestDTO.builder()
                .brand("Renault")
                .fuelType(FuelType.GASOLINE)
                .yearOfManufacture(2020)
                .build();
    }

    @Test
    void testAddVehicleToGarage() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(brandRepository.findByName("Renault")).thenReturn(Optional.of(brand));
        when(vehicleMapper.toEntity(vehicleRequestDTO)).thenReturn(vehicle);
        when(vehicleMapper.toResponseDto(vehicle)).thenReturn(
                new VehicleResponseDTO(vehicle.getVehicleId(), "Renault", vehicle.getYearOfManufacture(), vehicle.getFuelType(), null, null)
        );

        VehicleResponseDTO response = vehicleService.addVehicleToGarage(1L, vehicleRequestDTO);

        assertThat(response.brand()).isEqualTo("Renault");
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testUpdateVehicle() {
        when(vehicleRepository.findByVehicleIdAndGarage_GarageId(1L, 1L)).thenReturn(Optional.of(vehicle));
        when(brandRepository.findByName("Renault")).thenReturn(Optional.of(brand));
        when(vehicleMapper.toEntity(vehicleRequestDTO)).thenReturn(vehicle);
        when(vehicleMapper.toResponseDto(vehicle)).thenReturn(
                new VehicleResponseDTO(vehicle.getVehicleId(), "Renault", vehicle.getYearOfManufacture(), vehicle.getFuelType(), null, null)
        );

        VehicleResponseDTO response = vehicleService.updateVehicle(1L, 1L, vehicleRequestDTO);

        assertThat(response.brand()).isEqualTo("Renault");
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testDeleteVehicle() {
        when(vehicleRepository.findByVehicleIdAndGarage_GarageId(1L, 1L)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(1L, 1L);

        verify(vehicleRepository, times(1)).delete(vehicle);
    }

    @Test
    void testFindVehicleByGarageId() {
        garage.getVehicles().add(vehicle);
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleMapper.toResponseDto(vehicle)).thenReturn(
                new VehicleResponseDTO(vehicle.getVehicleId(), "Renault", vehicle.getYearOfManufacture(), vehicle.getFuelType(), null, null)
        );

        Set<VehicleResponseDTO> result = vehicleService.findVehicleByGarageId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void testVehicleNotFound() {
        when(vehicleRepository.findByVehicleIdAndGarage_GarageId(1L, 1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> vehicleService.updateVehicle(1L, 1L, vehicleRequestDTO));
    }
}

