package com.example.Employee.Leave.Management.System.service;

import com.example.Employee.Leave.Management.System.dto.LeaveRequestDTO;
import com.example.Employee.Leave.Management.System.exception.NotFoundException;
import com.example.Employee.Leave.Management.System.model.*;
import com.example.Employee.Leave.Management.System.repository.EmployeeRepository;
import com.example.Employee.Leave.Management.System.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeRepository empRepo;
    private final EmailService emailService;

    public LeaveRequestDTO applyLeave(Long empId, LeaveRequest leave) {

        if (leave.getFromDate() == null || leave.getToDate() == null ||
                leave.getFromDate().isAfter(leave.getToDate()))
            throw new IllegalArgumentException("Invalid date range");

        Employee emp = empRepo.findById(empId)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        int days = (int) ChronoUnit.DAYS.between(
                leave.getFromDate(), leave.getToDate()) + 1;

        if (emp.getLeaveBalance() < days)
            throw new IllegalArgumentException ("Insufficient leave balance");

        leave.setEmployee(emp);
        leave.setDays(days);
        leave.setStatus(LeaveStatus.PENDING);

        LeaveRequest saved = leaveRepo.save(leave);

       /* if (emp.getManagerId() != null)
            empRepo.findById(emp.getManagerId()).ifPresent(m ->
                    emailService.sendSimple(
                            m.getEmail(),
                            "Leave Approval Request",
                            emp.getName() + " applied leave from " +
                                    leave.getFromDate() + " to " + leave.getToDate()
                    )); */

        return mapToDTO(saved);
    }

    public LeaveRequestDTO approveLeave(Long leaveId, Long managerId, boolean approve) {

        LeaveRequest leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new NotFoundException("Leave not found"));

        Employee emp = leave.getEmployee();

        if (!managerId.equals(emp.getManagerId()))
            throw new IllegalArgumentException("Unauthorized action");

        leave.setStatus(approve ? LeaveStatus.APPROVED : LeaveStatus.REJECTED);

        if (approve) {
            emp.setLeaveBalance(emp.getLeaveBalance() - leave.getDays());
            empRepo.save(emp);
        }

        leaveRepo.save(leave);

     /*   emailService.sendSimple(
                emp.getEmail(),
                approve ? "Leave Approved" : "Leave Rejected",
                "Your leave from " + leave.getFromDate() + " to " +
                        leave.getToDate() + " has been " +
                        (approve ? "APPROVED." : "REJECTED.")
        );*/

        return mapToDTO(leave);
    }

    public LeaveRequestDTO updateLeave(Long leaveId, LeaveRequestDTO dto) {

        LeaveRequest leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new NotFoundException("Leave not found"));

        if (leave.getStatus() != LeaveStatus.PENDING)
            throw new IllegalArgumentException("Only PENDING leave can be updated");

        int newDays = (int) ChronoUnit.DAYS.between(
                dto.getFromDate(), dto.getToDate()) + 1;

        Employee emp = leave.getEmployee();

        if (newDays > leave.getDays() &&
                emp.getLeaveBalance() < (newDays - leave.getDays()))
            throw new IllegalArgumentException("Insufficient balance");

        leave.setFromDate(dto.getFromDate());
        leave.setToDate(dto.getToDate());
        leave.setReason(dto.getReason());
        leave.setDays(newDays);

        log.info("Leave updated for employee {}", emp.getId());
        return mapToDTO(leaveRepo.save(leave));
    }

    public List<LeaveRequestDTO> getLeavesByStatus(LeaveStatus status) {
        return leaveRepo.findByStatus(status).stream()
                .map(this::mapToDTO).toList();
    }

    public List<LeaveRequestDTO> getLeavesByEmployee(Long empId) {
        return leaveRepo.findByEmployeeId(empId).stream()
                .map(this::mapToDTO).toList();
    }

    private LeaveRequestDTO mapToDTO(LeaveRequest leave) {
        return new LeaveRequestDTO(
                leave.getId(),
                leave.getEmployee().getId(),
                leave.getFromDate(),
                leave.getToDate(),
                leave.getReason(),
                leave.getStatus(),
                leave.getDays()
        );
    }

    @Configuration
    public static class Javamailsender {

        @Bean
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);
            mailSender.setUsername("yourgmail@gmail.com");
            mailSender.setPassword("your16characterapppassword");

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            return mailSender;
        }
    }
}
