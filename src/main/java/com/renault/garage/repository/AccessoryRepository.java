package com.renault.garage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renault.garage.entity.Accessory;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {}
