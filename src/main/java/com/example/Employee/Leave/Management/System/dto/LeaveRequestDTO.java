package com.example.Employee.Leave.Management.System.dto;


import com.example.Employee.Leave.Management.System.model.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class LeaveRequestDTO {
    private Long id;
    private Long employeeId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private LeaveStatus status;
    private int days;
}
