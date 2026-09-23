package com.employee_management.employee_management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.dto.EmployeeForm;
import com.employee_management.employee_management.service.DepartmentService;
import com.employee_management.employee_management.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

  private final EmployeeService employeeService;
  private final DepartmentService departmentService;

  public EmployeeViewController(EmployeeService employeeService, DepartmentService departmentService) {
    this.employeeService = employeeService;
    this.departmentService = departmentService;
  }

  @GetMapping("/list")
  public String list(@RequestParam(required = false) String name, Model model) {
    List<Employee> employees = name != null
        ? employeeService.searchByName(name)
        : employeeService.findAll();

    model.addAttribute("employees", employees);
    model.addAttribute("name", name);

    return "employees/list";
  }

  @GetMapping("/add")
  public String showAddForm(Model model) {
    model.addAttribute("employeeForm", new EmployeeForm());
    model.addAttribute("departments", departmentService.findAll());

    return "employees/add";
  }

  @PostMapping("/add")
  public String addEmployee(@Valid @ModelAttribute("employeeForm") EmployeeForm form, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("departments", departmentService.findAll());

      return "employees/add";
    }

    employeeService.createEmployee(form.getName(), form.getEmail(), form.getDepartmentId());

    return "redirect:/employees/list";
  }
}
