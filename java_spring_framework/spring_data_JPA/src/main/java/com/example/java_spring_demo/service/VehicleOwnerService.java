package com.example.java_spring_demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.java_spring_demo.entity.VehicleOwner;
import com.example.java_spring_demo.repository.VehicleOwnerRepository;
import com.example.java_spring_demo.repository.VehicleRepository;

@Service
public class VehicleOwnerService {
  private final VehicleOwnerRepository repo;
  private final VehicleRepository vehicleRepo;

  public VehicleOwnerService(VehicleOwnerRepository repo, VehicleRepository vehicleRepo) {
    this.repo = repo;
    this.vehicleRepo = vehicleRepo;
  }

  public List<VehicleOwner> findAll() {
    return repo.findAll();
  }

  public VehicleOwner findById(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + id));
  }

  public VehicleOwner findByIdNumber(String idNumber) {
    return repo.findByIdNumber(idNumber)
        .orElseThrow(() -> new IllegalArgumentException(
            "Owner not found with id number: " + idNumber));
  }

  // Transaction: 3 lệnh xuống DB (check idNumber + check email + insert)
  @Transactional
  public VehicleOwner create(VehicleOwner owner) {
    if (repo.existsByIdNumber(owner.getIdNumber())) {
      throw new IllegalArgumentException("Id number already exists: " + owner.getIdNumber());
    }

    if (repo.existsByEmail(owner.getEmail())) {
      throw new IllegalArgumentException("Email already exists: " + owner.getEmail());
    }

    return repo.save(owner);
  }
}
