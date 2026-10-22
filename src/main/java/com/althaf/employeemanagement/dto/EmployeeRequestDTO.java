package com.althaf.employeemanagement.dto;

import com.althaf.employeemanagement.enums.EmployeeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDTO {

    @NotBlank( message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank( message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email( message = "Please provide a valid Email")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Department is required")
    @Size(max = 50)
    private String department;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than Zero")
    private Double salary;

    @NotNull(message = "Employee Status Is Required")
    @Schema(description = "The current working status of the employee",example = "ACTIVE")
    private EmployeeStatus status;
}
