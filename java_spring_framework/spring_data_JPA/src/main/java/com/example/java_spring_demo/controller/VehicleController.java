package com.example.java_spring_demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_spring_demo.entity.Vehicle;
import com.example.java_spring_demo.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
  private final VehicleService service;

  public VehicleController(VehicleService service) {
    this.service = service;
  }

  // GET /api/vehicles
  @GetMapping
  public List<Vehicle> findAll() {
    return service.findAll();
  }

  // GET /api/vehicles/1
  @GetMapping("/{id}")
  public Vehicle findById(@PathVariable Long id) {
    return service.findById(id);
  }

  // GET /api/vehicles/search?vehicleNumber=51A01
  @GetMapping("/search")
  public Vehicle findByVehicleNumber(@RequestParam String vehicleNumber) {
    return service.findByVehicleNumber(vehicleNumber);
  }

  // POST /api/vehicles
  @PostMapping
  public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {
    Vehicle created = service.create(vehicle);

    return ResponseEntity
        .created(URI.create("/api/vehicles/" + created.getId()))
        .body(created);
  }
}
