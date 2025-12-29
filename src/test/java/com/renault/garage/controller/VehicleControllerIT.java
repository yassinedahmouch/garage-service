package com.renault.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.garage.AddressDTO;
import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.vehicle.VehicleRequestDTO;
import com.renault.garage.enums.FuelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long garageId;

    @BeforeEach
    void setup() throws Exception {
        // Create Garage for all tests
        AddressDTO address = new AddressDTO("Street 1", "Paris", "75001", "France");
        GarageRequestDTO garageRequestDTO = GarageRequestDTO.builder()
                .garageName("Garage IT")
                .phoneNumber("0123456789")
                .email("garage@it.com")
                .address(address)
                .openingHours(Map.of(DayOfWeek.MONDAY, List.of(
                        new com.renault.garage.dto.garage.OpeningTimeDTO(LocalTime.of(8,0), LocalTime.of(18,0))
                )))
                .build();

        String garageResponse = mockMvc.perform(post("/garages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        garageId = objectMapper.readTree(garageResponse).get("garageId").asLong();
    }

    private VehicleRequestDTO createVehicleRequest(String brand, int year, FuelType fuelType) {
        return VehicleRequestDTO.builder()
                .brand(brand)
                .yearOfManufacture(year)
                .fuelType(fuelType)
                .build();
    }

    @Test
    void testAddVehicle() throws Exception {
        VehicleRequestDTO vehicleRequest = createVehicleRequest("Renault", 2020, FuelType.GASOLINE);

        mockMvc.perform(post("/garages/{garageId}/vehicles", garageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2020));
    }

    @Test
    void testUpdateVehicle() throws Exception {
        // Add first
        VehicleRequestDTO addRequest = createVehicleRequest("Renault", 2020, FuelType.GASOLINE);
        String addResponse = mockMvc.perform(post("/garages/{garageId}/vehicles", garageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequest)))
                .andReturn().getResponse().getContentAsString();

        Long vehicleId = objectMapper.readTree(addResponse).get("vehicleId").asLong();

        // Update
        VehicleRequestDTO updateRequest = createVehicleRequest("Peugeot", 2021, FuelType.DIESEL);
        mockMvc.perform(put("/garages/{garageId}/vehicles/{vehicleId}", garageId, vehicleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Peugeot"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2021));
    }

    @Test
    void testGetVehicles() throws Exception {
        VehicleRequestDTO vehicleRequest = createVehicleRequest("Renault", 2020, FuelType.GASOLINE);
        mockMvc.perform(post("/garages/{garageId}/vehicles", garageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/garages/{garageId}/vehicles", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Renault"));
    }

    @Test
    void testDeleteVehicle() throws Exception {
        VehicleRequestDTO vehicleRequest = createVehicleRequest("Renault", 2020, FuelType.GASOLINE);
        String addResponse = mockMvc.perform(post("/garages/{garageId}/vehicles", garageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andReturn().getResponse().getContentAsString();

        Long vehicleId = objectMapper.readTree(addResponse).get("vehicleId").asLong();

        mockMvc.perform(delete("/garages/{garageId}/vehicles/{vehicleId}", garageId, vehicleId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/garages/{garageId}/vehicles", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
