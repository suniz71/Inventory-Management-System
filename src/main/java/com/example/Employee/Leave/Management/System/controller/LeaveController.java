package com.example.Employee.Leave.Management.System.controller;


import com.example.Employee.Leave.Management.System.dto.LeaveRequestDTO;
import com.example.Employee.Leave.Management.System.model.LeaveRequest;
import com.example.Employee.Leave.Management.System.model.LeaveStatus;
import com.example.Employee.Leave.Management.System.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService service;

    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<LeaveRequestDTO> apply(@PathVariable Long employeeId,
                                                 @Validated @RequestBody LeaveRequest lr) {
        return ResponseEntity.ok(service.applyLeaveDTO(employeeId, lr));
    }

    @PostMapping("/{leaveId}/approve")
    public ResponseEntity<LeaveRequestDTO> approve(@PathVariable Long leaveId,
                                                   @RequestParam Long approverId) {
        return ResponseEntity.ok(service.approveDTO(leaveId, approverId, true));
    }

    @PostMapping("/{leaveId}/reject")
    public ResponseEntity<LeaveRequestDTO> reject(@PathVariable Long leaveId,
                                                  @RequestParam Long approverId) {
        return ResponseEntity.ok(service.approveDTO(leaveId, approverId, false));
    }

    @GetMapping("/status")
    public List<LeaveRequestDTO> byStatus(@RequestParam LeaveStatus status) {
        return service.searchByStatusDTO(status);
    }

    @GetMapping("/between")
    public List<LeaveRequestDTO> between(@RequestParam String start, @RequestParam String end) {
        return service.findBetweenDTO(LocalDate.parse(start), LocalDate.parse(end));
    }

    @GetMapping("/employee/{id}")
    public List<LeaveRequestDTO> byEmployee(@PathVariable Long id) {
        return service.byEmployeeDTO(id);
    }
}
