package com.test.test.service;

import com.test.test.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findEmployee(Long id);

    List<Employee> fiendAllEmployee();

    Employee save(Employee employee);

    boolean delete(Long id);
}
