package com.althaf.employeemanagement.dto;

import com.althaf.employeemanagement.enums.EmployeeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Double salary;

    @Schema(description = "The current working status of the employee",example = "ACTIVE")
    private EmployeeStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}