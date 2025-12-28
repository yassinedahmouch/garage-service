package com.renault.garage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.renault.garage.entity.Garage;

public interface GarageRepository  extends JpaRepository<Garage, Long>, JpaSpecificationExecutor<Garage>{

}
