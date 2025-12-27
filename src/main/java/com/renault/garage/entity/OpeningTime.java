package com.renault.garage.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
public class OpeningTime {

    @Column(name ="start_time")
    private LocalTime startTime;
    
    @Column(name ="end_time")
    private LocalTime endTime;
}
