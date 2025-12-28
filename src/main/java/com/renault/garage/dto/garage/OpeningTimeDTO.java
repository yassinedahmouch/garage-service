package com.renault.garage.dto.garage;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningTimeDTO {

    private LocalTime startTime;
    
    private LocalTime endTime;
}

