package com.example.java_spring_demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_spring_demo.entity.VehicleOwner;
import com.example.java_spring_demo.service.VehicleOwnerService;

@RestController
@RequestMapping("/api/vehicle-owners")
public class VehicleOwnerController {
  private final VehicleOwnerService service;

  public VehicleOwnerController(VehicleOwnerService service) {
    this.service = service;
  }

  @GetMapping
  public List<VehicleOwner> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public VehicleOwner findById(@PathVariable Long id) {
    return service.findById(id);
  }

  // GET /api/vehicle-owners/search?idNumber=012345678901
  @GetMapping("/search")
  public VehicleOwner findByIdNumber(@RequestParam String idNumber) {
    return service.findByIdNumber(idNumber);
  }

  @PostMapping
  public ResponseEntity<VehicleOwner> create(@RequestBody VehicleOwner owner) {
    VehicleOwner created = service.create(owner);

    return ResponseEntity
        .created(URI.create("/api/vehicle-owners/" + created.getId()))
        .body(created);
  }
}
