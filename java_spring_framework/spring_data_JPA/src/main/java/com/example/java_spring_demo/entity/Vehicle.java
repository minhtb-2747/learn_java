package com.example.java_spring_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
  private Long id;

  @Column(name = "vehicle_number", nullable = false, length = 5)
  private String vehicleNumber;

  @Column(name = "manufacturer", nullable = false)
  private String manufacturer;

  @Column(name = "year_of_manufacture", nullable = false)
  private int yearOfManufacture;

  @Column(name = "color", nullable = false)
  private String color;

  @ManyToOne(optional = false) // 1 vehicle can have 1 owner, but 1 owner can have many vehicles
  @JoinColumn(name = "vehicle_owner_id", nullable = false)
  private VehicleOwner vehicleOwner;

  public Vehicle() {
  }

  public Vehicle(String vehicleNumber, String manufacturer, int yearOfManufacture, String color,
      VehicleOwner vehicleOwner) {
    this.vehicleNumber = vehicleNumber;
    this.manufacturer = manufacturer;
    this.yearOfManufacture = yearOfManufacture;
    this.color = color;
    this.vehicleOwner = vehicleOwner;
  }


  public Long getId() {
    return id;
  }

  public String getVehicleNumber() {
    return vehicleNumber;
  }

  public void setVehicleNumber(String vehicleNumber) {
    this.vehicleNumber = vehicleNumber;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public int getYearOfManufacture() {
    return yearOfManufacture;
  }

  public void setYearOfManufacture(int yearOfManufacture) {
    this.yearOfManufacture = yearOfManufacture;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public VehicleOwner getVehicleOwner() {
    return vehicleOwner;
  }

  public void setVehicleOwner(VehicleOwner vehicleOwner) {
    this.vehicleOwner = vehicleOwner;
  }
}
