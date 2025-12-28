package com.renault.garage.dto.garage;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record GarageResponseDTO(Long garageId,
                                String garageName,
                                AddressDTO address,
                                String phoneNumber,
                                String email,
                                Map<DayOfWeek, List<OpeningTimeDTO>> openingHours) {

}
