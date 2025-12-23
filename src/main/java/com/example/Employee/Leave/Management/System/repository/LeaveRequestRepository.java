package com.example.Employee.Leave.Management.System.repository;


import com.example.Employee.Leave.Management.System.model.LeaveRequest;
import com.example.Employee.Leave.Management.System.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface  LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByFromDateBetween(LocalDate start , LocalDate end);
}
