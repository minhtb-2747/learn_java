package com.example.java_spring_demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.java_spring_demo.entity.Vehicle;
import com.example.java_spring_demo.repository.VehicleRepository;

@Service
public class VehicleService {
  private final VehicleRepository repo;

  public VehicleService(VehicleRepository repo) {
    this.repo = repo;
  }

  public List<Vehicle> findAll() {
    return repo.findAll();
  }

  public Vehicle findById(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
  }

  public Vehicle findByVehicleNumber(String vehicleNumber) {
    return repo.findByVehicleNumber(vehicleNumber)
        .orElseThrow(() -> new IllegalArgumentException(
            "Vehicle not found with number: " + vehicleNumber));
  }

  // Transaction: 2 lệnh xuống DB (check trung bien so + insert)
  @Transactional
  public Vehicle create(Vehicle vehicle) {
    if (repo.existsByVehicleNumber(vehicle.getVehicleNumber())) {
      throw new IllegalArgumentException(
          "Vehicle number already exists: " + vehicle.getVehicleNumber());
    }

    return repo.save(vehicle);
  }
}
