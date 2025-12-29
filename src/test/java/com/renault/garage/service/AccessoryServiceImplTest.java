package com.renault.garage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.renault.garage.dto.accessory.AccessoryRequestDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.exception.BadRequestException;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.implementation.AccessoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccessoryServiceImplTest {

    @Mock
    private VehicleRepository    vehicleRepository;

    @Mock
    private AccessoryRepository  accessoryRepository;

    @Mock
    private AccessoryMapper      accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoryService;

    private Vehicle              vehicle;
    private Accessory            accessory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setAccessories(new HashSet<>());

        accessory = new Accessory();
        accessory.setAccessoryId(1L);
        accessory.setVehicles(new HashSet<>());
    }

    @Test
    void testAddAccessoryToVehicle() {
        AccessoryRequestDTO request = new AccessoryRequestDTO("AC", "Description", 100, AccessoryType.DASHBOARD_CAMERA);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(request)).thenReturn(accessory);
        when(accessoryMapper.toResponseDto(accessory))
                .thenReturn(mock(com.renault.garage.dto.accessory.AccessoryResponseDTO.class));
        when(accessoryRepository.save(accessory)).thenReturn(accessory);

        assertNotNull(accessoryService.addAccessoryToVehicle(1L, request));
        assertTrue(vehicle.getAccessories().contains(accessory));
    }

    @Test
    void testUpdateAccessory() {
        AccessoryRequestDTO request = new AccessoryRequestDTO("Updated", "Desc", 200, AccessoryType.SUNSHADES);
        vehicle.getAccessories().add(accessory);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toResponseDto(accessory))
                .thenReturn(mock(com.renault.garage.dto.accessory.AccessoryResponseDTO.class));

        assertNotNull(accessoryService.updateAccessory(1L, 1L, request));
        assertEquals("Updated", accessory.getName());
    }

    @Test
    void testRemoveAccessoryFromVehicle() {
        vehicle.getAccessories().add(accessory);
        accessory.getVehicles().add(vehicle);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        accessoryService.removeAccessoryFromVehicle(1L, 1L);
        assertFalse(vehicle.getAccessories().contains(accessory));
        assertFalse(accessory.getVehicles().contains(vehicle));
    }

    @Test
    void testGetAccessoryByVehicle() {
        vehicle.getAccessories().add(accessory);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toResponseDto(accessory))
                .thenReturn(mock(com.renault.garage.dto.accessory.AccessoryResponseDTO.class));

        Set<com.renault.garage.dto.accessory.AccessoryResponseDTO> result = accessoryService.getAccessoryByVehicule(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteAccessorySuccess() {
        accessory.setVehicles(new HashSet<>());
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

        assertDoesNotThrow(() -> accessoryService.deleteAccessory(1L));
        verify(accessoryRepository).delete(accessory);
    }

    @Test
    void testDeleteAccessoryInUseThrows() {
        accessory.getVehicles().add(vehicle);
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

        assertThrows(BadRequestException.class, () -> accessoryService.deleteAccessory(1L));
    }

    @Test
    void testVehicleNotFoundThrows() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        AccessoryRequestDTO request = new AccessoryRequestDTO("AC", "Desc", 100, AccessoryType.CAR_COVER);

        assertThrows(NotFoundException.class, () -> accessoryService.addAccessoryToVehicle(1L, request));
    }
}
