package com.example.Employee.Leave.Management.System.controller;

import com.example.Employee.Leave.Management.System.model.Employee;
import com.example.Employee.Leave.Management.System.repository.EmployeeRepository;
import com.example.Employee.Leave.Management.System.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repo;
    private final EmailService emailService;

    // ✅ CREATE EMPLOYEE
    @PostMapping
    public Employee create(@RequestBody Employee e) {

        Employee savedEmployee = repo.save(e);

        emailService.sendSimple(
                savedEmployee.getEmail(),
                "Welcome to the Company",
                "Hi " + savedEmployee.getName() +
                        ",\n\nYour employee profile has been created successfully."
        );

        return savedEmployee;
    }

    // ✅ GET ALL EMPLOYEES
    @GetMapping
    public List<Employee> all() {
        return repo.findAll();
    }

    // ✅ GET EMPLOYEE BY ID
    @GetMapping("/{id}")
    public Employee one(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // ✅ UPDATE EMPLOYEE
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee updatedEmployee) {

        return repo.findById(id)
                .map(emp -> {
                    emp.setName(updatedEmployee.getName());
                    emp.setEmail(updatedEmployee.getEmail());
                    emp.setLeaveBalance(updatedEmployee.getLeaveBalance());
                    emp.setManagerId(updatedEmployee.getManagerId());

                    Employee saved = repo.save(emp);

                    emailService.sendSimple(
                            saved.getEmail(),
                            "Profile Updated",
                            "Hi " + saved.getName() +
                                    ",\n\nYour employee profile has been updated successfully.");
                                return saved;
                })
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // ✅ DELETE EMPLOYEE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        Employee emp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        repo.deleteById(id);

        emailService.sendSimple(
                emp.getEmail(),
                "Account Deleted",
                "Hi " + emp.getName() +
                        ",\n\nYour employee account has been deleted."
        );

        return "Deleted successfully";
    }
}
