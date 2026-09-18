package com.example.java_spring_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle_owner")
public class VehicleOwner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
  private Long id;

  @Column(name = "id_number", nullable = false, unique = true, length = 12)
  private String idNumber;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  public VehicleOwner() {
  }

  public VehicleOwner(String idNumber, String fullName, String email) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.email = email;
  }


  public Long getId() {
    return id;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
