package com.renault.garage.dto.garage;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GarageRequestDTO {

    
    @NotBlank
    @Size(max = 80)
    private String garageName;

    @NotNull
    private AddressDTO address;

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @Email
    @NotBlank
    @Size(max = 40)
    private String email;

    @NotNull
    private Map<DayOfWeek, List<OpeningTimeDTO>> openingHours;

 }
