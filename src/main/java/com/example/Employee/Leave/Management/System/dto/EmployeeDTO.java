package com.example.Employee.Leave.Management.System.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private int leaveBalance;
    private Long managerId;
}
