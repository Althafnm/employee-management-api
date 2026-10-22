package com.althaf.employeemanagement.service;

import com.althaf.employeemanagement.dto.EmployeeRequestDTO;
import com.althaf.employeemanagement.dto.EmployeeResponseDTO;
import com.althaf.employeemanagement.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO);

    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO);

    void deleteEmployee(Long id);

    Page<EmployeeResponseDTO> searchEmployees(String keyword, Pageable pageable);

    Page<EmployeeResponseDTO> getEmployeeByStatus(EmployeeStatus status,Pageable pageable);


}
