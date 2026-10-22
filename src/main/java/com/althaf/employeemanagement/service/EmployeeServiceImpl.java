package com.althaf.employeemanagement.service;

import com.althaf.employeemanagement.dto.EmployeeRequestDTO;
import com.althaf.employeemanagement.dto.EmployeeResponseDTO;
import com.althaf.employeemanagement.entity.Employee;
import com.althaf.employeemanagement.enums.EmployeeStatus;
import com.althaf.employeemanagement.exception.DuplicateResourceException;
import com.althaf.employeemanagement.exception.ResourceNotFoundException;
import com.althaf.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private final EmployeeRepository employeeRepository;

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        if (employeeRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Employee With Email " + requestDTO.getEmail() + " already exists");
        }
        Employee employee = mapToEntity(requestDTO);
        return mapToResponseDTO(employeeRepository.save(employee));

    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::mapToResponseDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee Not Found With ID:"+id));
        return mapToResponseDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {

        Employee existing = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee Not Found With The ID:"+id));
        if(employeeRepository.existsByEmailAndIdNot(requestDTO.getEmail(),id)) {

            throw new DuplicateResourceException("Another Employee With Email "+requestDTO.getEmail()+"already exists");
        }
        existing.setFirstName(requestDTO.getFirstName());
        existing.setLastName(requestDTO.getLastName());
        existing.setEmail(requestDTO.getEmail());
        existing.setDepartment(requestDTO.getDepartment());
        existing.setSalary(requestDTO.getSalary());
        existing.setStatus(requestDTO.getStatus());

        return mapToResponseDTO(employeeRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {

        Employee existing = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee Not Found With The ID:" +id));
        employeeRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> searchEmployees(String keyword, Pageable pageable) {
        if(keyword == null || keyword.trim().isEmpty()) {

            return employeeRepository.findAll(pageable).map(this::mapToResponseDTO);
        }
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(keyword,keyword,pageable).map(this::mapToResponseDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployeeByStatus(EmployeeStatus status, Pageable pageable) {
        return employeeRepository.findByStatus(status,pageable).map(this::mapToResponseDTO);
    }


    private Employee mapToEntity(EmployeeRequestDTO dto) {
        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .salary(dto.getSalary())
                .status(dto.getStatus() != null ? dto.getStatus(): EmployeeStatus.ACTIVE)
                .build();
    }

    private EmployeeResponseDTO mapToResponseDTO(Employee entity) {
        return EmployeeResponseDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .department(entity.getDepartment())
                .salary(entity.getSalary())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
