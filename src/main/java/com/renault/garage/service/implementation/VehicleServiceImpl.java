package com.renault.garage.service.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renault.garage.dto.vehicle.VehicleRequestDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.dto.vehicle.VehicleSearchDTO;
import com.renault.garage.entity.Brand;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.BadRequestException;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.BrandRepository;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.VehicleService;
import com.renault.garage.utlis.PageableUtil;
import com.renault.garage.utlis.VehicleSpecificationUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService{
    
    private static final int MAX_VEHICLES_PER_GARAGE = 50;

    private final VehicleRepository vehicleRepository;
    private final BrandRepository brandRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public VehicleResponseDTO addVehicleToGarage(Long garageId, VehicleRequestDTO vehicleRequestDTO) {
        Garage garage = findGarage(garageId);
        Brand brand = findOrCreateBrand(vehicleRequestDTO.getBrand());       
        Vehicle vehicle = vehicleMapper.toEntity(vehicleRequestDTO);
        
        vehicle.setBrand(brand);
        garage.addVehicle(vehicle);
        
        vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDto(vehicle);
    }
    @Override
    @Transactional
    public VehicleResponseDTO updateVehicle(Long garageId, Long vehicleId, VehicleRequestDTO vehicleRequestDTO) {
        Vehicle existingVehicle = findVehicleInGarage(vehicleId, garageId);
        Brand brand = findOrCreateBrand(vehicleRequestDTO.getBrand()); 
        Vehicle updatedVehicle = vehicleMapper.toEntity(vehicleRequestDTO);
        
        updatedVehicle.setVehicleId(existingVehicle.getVehicleId());
        updatedVehicle.setGarage(existingVehicle.getGarage());
        updatedVehicle.setAccessories(existingVehicle.getAccessories());
        updatedVehicle.setBrand(brand);
        
        vehicleRepository.save(updatedVehicle);

        return vehicleMapper.toResponseDto(updatedVehicle);
    }
    @Override
    @Transactional
    public void deleteVehicle(Long garageId, Long vehicleId) {
        Vehicle vehicle = findVehicleInGarage(vehicleId, garageId);
        Garage garage = vehicle.getGarage();
        garage.getVehicles().remove(vehicle);   
        vehicleRepository.delete(vehicle);

    }
    @Override
    @Transactional(readOnly = true)
    public Set<VehicleResponseDTO> findVehicleByGarageId(Long garageId) {
        Garage garage = findGarage(garageId);
        Set<Vehicle> vehicles = garage.getVehicles();
        Set<VehicleResponseDTO> vehiclesResponseDTO = vehicles.stream()
                                                              .map(vehicule -> vehicleMapper.toResponseDto(vehicule))
                                                              .collect(Collectors.toSet());
        return vehiclesResponseDTO;
    }
   
    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponseDTO> search(VehicleSearchDTO criteria) {

        Specification<Vehicle> spec = VehicleSpecificationUtil.byCriteria(
                criteria.getBrand(),
                criteria.getGarageId()
        );

        Pageable pageable = PageableUtil.buildPageable(
                criteria.getPage(),
                criteria.getSize(),
                criteria.getSort()
        );

        return vehicleRepository.findAll(spec, pageable)
                .map(vehicule -> vehicleMapper.toResponseDto(vehicule));
    }
    
    private Garage findGarage(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                                     .orElseThrow(() -> new NotFoundException("Garage not found for the given Id"));

        if (garage.getVehicles().size() >= MAX_VEHICLES_PER_GARAGE) {
          throw new BadRequestException("The garage has reached the maximum of storage capacity", "CAPACITY_REACHED");
        }
        return garage;
      }
    
      private Brand findOrCreateBrand(String brandName) {
          return brandRepository.findByName(brandName).orElseGet(() -> {
              Brand newModel = new Brand();
              newModel.setName(brandName);
              return brandRepository.save(newModel);
          });
      }
      
      private Vehicle findVehicleInGarage(Long vehicleId, Long garageId) {
          return vehicleRepository.findByVehicleIdAndGarage_GarageId(vehicleId, garageId)
                  .orElseThrow(() -> new NotFoundException("Vehicle not found in this garage"));
      }
}
