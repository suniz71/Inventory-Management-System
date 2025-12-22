package com.example.Employee.Leave.Management.System.controller;

import com.example.Employee.Leave.Management.System.dto.LeaveRequestDTO;
import com.example.Employee.Leave.Management.System.model.LeaveRequest;
import com.example.Employee.Leave.Management.System.model.LeaveStatus;
import com.example.Employee.Leave.Management.System.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService service;

    // APPLY LEAVE
    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<LeaveRequestDTO> apply(
            @PathVariable Long employeeId,
            @RequestBody LeaveRequest request) {

        return ResponseEntity.ok(service.applyLeave(employeeId, request));
    }

    // APPROVE LEAVE
    @PostMapping("/{leaveId}/approve")
    public ResponseEntity<LeaveRequestDTO> approve(
            @PathVariable Long leaveId,
            @RequestParam Long approverId) {

        return ResponseEntity.ok(service.approveLeave(leaveId, approverId, true));
    }

    // REJECT LEAVE
    @PostMapping("/{leaveId}/reject")
    public ResponseEntity<LeaveRequestDTO> reject(
            @PathVariable Long leaveId,
            @RequestParam Long approverId) {

        return ResponseEntity.ok(service.approveLeave(leaveId, approverId, false));
    }

    // GET BY STATUS
    @GetMapping("/status")
    public List<LeaveRequestDTO> byStatus(@RequestParam LeaveStatus status) {
        return service.getLeavesByStatus(status);
    }

    // GET BY EMPLOYEE
    @GetMapping("/employee/{id}")
    public List<LeaveRequestDTO> byEmployee(@PathVariable  Long id) {

        return service.getLeavesByEmployee(id);
    }

    // UPDATE LEAVE
    @PutMapping("/{leaveId}")
    public ResponseEntity<LeaveRequestDTO> update(
            @PathVariable Long leaveId,
            @RequestBody LeaveRequestDTO dto) {

        return ResponseEntity.ok(service.updateLeave(leaveId, dto));
    }
}
