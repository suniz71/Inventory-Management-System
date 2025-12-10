package com.example.Employee.Leave.Management.System.controller;

import com.example.Employee.Leave.Management.System.dto.EmployeeDTO;
import com.example.Employee.Leave.Management.System.model.Employee;
import com.example.Employee.Leave.Management.System.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repo;

    private EmployeeDTO toDTO(Employee e) {
        return EmployeeDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .email(e.getEmail())
                .leaveBalance(e.getLeaveBalance())
                .managerId(e.getManagerId())
                .build();
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Validated @RequestBody Employee e) {
        return ResponseEntity.ok(toDTO(repo.save(e)));
    }

    @GetMapping
    public List<EmployeeDTO> list(@RequestParam(required = false) String q) {
        var list = q != null ? repo.findByNameContainingIgnoreCase(q) : repo.findAll();
        return list.stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> get(@PathVariable Long id) {
        return repo.findById(id).map(e -> ResponseEntity.ok(toDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @RequestBody Employee in) {
        return repo.findById(id).map(e -> {
            e.setName(in.getName());
            e.setEmail(in.getEmail());
            e.setManagerId(in.getManagerId());
            return ResponseEntity.ok(toDTO(repo.save(e)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
