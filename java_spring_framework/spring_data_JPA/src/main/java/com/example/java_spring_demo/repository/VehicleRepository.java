package com.example.java_spring_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.java_spring_demo.entity.Manufacturer;
import com.example.java_spring_demo.entity.Vehicle;

// không cần tự thêm @Repository do VehicleRepository extends JpaRepository, Spring Data JPA sẽ tự động tạo bean cho VehicleRepository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

  // Quy tắc đặt tên: find/exists/count/delete + By + tên field trong entity
  // (viết hoa chữ đầu), nối nhau bằng And / Or
  Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

  boolean existsByVehicleNumber(String vehicleNumber);

  List<Vehicle> findByManufacturer(Manufacturer manufacturer);

  List<Vehicle> findByColorAndYearOfManufacture(String color, int yearOfManufacture);

  List<Vehicle> findByVehicleOwnerId(Long ownerId);
}
