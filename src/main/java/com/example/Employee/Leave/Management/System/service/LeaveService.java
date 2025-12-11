package com.example.Employee.Leave.Management.System.service;

import com.example.Employee.Leave.Management.System.dto.LeaveRequestDTO;

import com.example.Employee.Leave.Management.System.model.LeaveRequest;
import com.example.Employee.Leave.Management.System.model.LeaveStatus;
import com.example.Employee.Leave.Management.System.repository.EmployeeRepository;

import com.example.Employee.Leave.Management.System.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeRepository empRepo;
    private final EmailService emailService;

    private LeaveRequestDTO toDTO(LeaveRequest lr) {
        return LeaveRequestDTO.builder()
                .id(lr.getId())
                .employeeId(lr.getEmployee().getId())
                .fromDate(lr.getFromDate())
                .toDate(lr.getToDate())
                .reason(lr.getReason())
                .status(lr.getStatus())
                .days(lr.getDays())
                .build();
    }

    public LeaveRequestDTO applyLeaveDTO(Long employeeId, LeaveRequest lr) {
        var emp = empRepo.findById(employeeId).orElseThrow(();

        long days = ChronoUnit.DAYS.between(lr.getFromDate(), lr.getToDate()) + 1;
        lr.setDays((int) days);
        if (days <= 0) throw new IllegalArgumentException("Invalid date range");
        if (emp.getLeaveBalance() < days) throw new IllegalArgumentException("Insufficient leave balance");

        lr.setStatus(LeaveStatus.PENDING);
        var saved = leaveRepo.save(lr);

        if (emp.getManagerId() != null) {
            empRepo.findById(emp.getManagerId()).ifPresent(manager ->
                    emailService.sendSimple(
                            manager.getEmail(),
                            "Leave approval required",
                            emp.getName() + " applied for leave from " + lr.getFromDate() + " to " + lr.getToDate()
                    )
            );
        }
        log.info("Leave applied by {} for {} days", emp.getId(), days);
        return toDTO(saved);
    }

    @Transactional
    public LeaveRequestDTO approveDTO(Long leaveId, Long approverId, boolean approve) {
        var lr = leaveRepo.findById(leaveId).orElseThrow(();

        if (emp.getManagerId() == null || !emp.getManagerId().equals(approverId))
            throw new IllegalArgumentException("Only manager can approve/reject");

        if (approve) {
            if (emp.getLeaveBalance() < lr.getDays())
                throw new IllegalArgumentException("Insufficient balance at approval time");
            emp.setLeaveBalance(emp.getLeaveBalance() - lr.getDays());
            lr.setStatus(LeaveStatus.APPROVED);
            emailService.sendSimple(emp.getEmail(), "Leave Approved", "Your leave has been approved.");
        } else {
            lr.setStatus(LeaveStatus.REJECTED);
            emailService.sendSimple(emp.getEmail(), "Leave Rejected", "Your leave request was rejected.");
        }
        empRepo.save(emp);
        return toDTO(lr);
    }

    public List<LeaveRequestDTO> searchByStatusDTO(LeaveStatus status) {
        return leaveRepo.findByStatus(status).stream().map(this::toDTO).toList();
    }

    public List<LeaveRequestDTO> findBetweenDTO(java.time.LocalDate start, java.time.LocalDate end) {
        return leaveRepo.findByFromDateBetween(start, end).stream().map(this::toDTO).toList();
    }

    public List<LeaveRequestDTO> byEmployeeDTO(Long empId) {
        return leaveRepo.findByEmployeeId(empId).stream().map(this::toDTO).toList();
    }
}
