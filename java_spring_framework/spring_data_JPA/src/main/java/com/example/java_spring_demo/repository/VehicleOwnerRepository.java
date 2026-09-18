package com.example.java_spring_demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.java_spring_demo.entity.VehicleOwner;

public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long> {

  Optional<VehicleOwner> findByIdNumber(String idNumber);

  boolean existsByIdNumber(String idNumber);

  boolean existsByEmail(String email);
}
