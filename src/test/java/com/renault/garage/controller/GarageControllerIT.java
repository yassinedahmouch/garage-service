package com.renault.garage.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.garage.AddressDTO;
import com.renault.garage.dto.garage.GarageRequestDTO;
import com.renault.garage.dto.garage.OpeningTimeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class GarageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private GarageRequestDTO garageRequestDTO;

    @BeforeEach
    void setup() {
        // Valid address
        AddressDTO address = new AddressDTO("123 Main St","Paris","75001", "France");

        OpeningTimeDTO openingTime = new OpeningTimeDTO();
        openingTime.setStartTime(LocalTime.of(8, 0));
        openingTime.setEndTime(LocalTime.of(18, 0));

        garageRequestDTO = GarageRequestDTO.builder()
                .garageName("Integration Garage")
                .phoneNumber("0123456789")
                .email("integration@example.com")
                .address(address) // must not be null
                .openingHours(Map.of(DayOfWeek.MONDAY, List.of(openingTime)))
                .build();
    }

    @Test
    void testCreateGarage() throws Exception {
        mockMvc.perform(post("/garages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.garageName").value("Integration Garage"));
    }

    @Test
    void testGetGarageById_NotFound() throws Exception {
        mockMvc.perform(get("/garages/{garageId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateGarage() throws Exception {
        // First, create a garage
        String response = mockMvc.perform(post("/garages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long createdGarageId = objectMapper.readTree(response).get("garageId").asLong();

        // Update the garage name
        garageRequestDTO.setGarageName("Updated Garage Name");

        mockMvc.perform(put("/garages/{garageId}", createdGarageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.garageName").value("Updated Garage Name"));
    }

    @Test
    void testDeleteGarage() throws Exception {
        // First, create a garage
        String response = mockMvc.perform(post("/garages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long createdGarageId = objectMapper.readTree(response).get("garageId").asLong();

        // Delete the garage
        mockMvc.perform(delete("/garages/{garageId}", createdGarageId))
                .andExpect(status().isNoContent());

        // Verify that getting the deleted garage returns 404
        mockMvc.perform(get("/garages/{garageId}", createdGarageId))
                .andExpect(status().isNotFound());
    }
}

