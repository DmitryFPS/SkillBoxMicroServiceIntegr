package com.test.test.repository;

import com.test.test.entity.Employee;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @NonNull
    List<Employee> findAll();
}