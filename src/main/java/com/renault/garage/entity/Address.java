package com.renault.garage.entity;

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
public class Address {

    private String country;
    private String city;
    private String zipCode;
    private String addressDetail;
}
