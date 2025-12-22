package com.example.Employee.Leave.Management.System.repository;


import com.example.Employee.Leave.Management.System.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface  EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNameContainingIgnoreCase(String name);
}
