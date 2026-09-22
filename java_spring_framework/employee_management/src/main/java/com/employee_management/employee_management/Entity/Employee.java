package com.employee_management.employee_management.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  protected Employee() {
  }

  public Employee(String name, String email, Department department) {
    this.name = name;
    this.email = email;
    this.department = department;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }
}
