package com.renault.garage.mapper;

import org.mapstruct.Mapper;

import com.renault.garage.dto.garage.AddressDTO;
import com.renault.garage.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressDTO dto);

    AddressDTO toDto(Address entity);
}
