package com.althaf.employeemanagement.service;

import com.althaf.employeemanagement.dto.EmployeeRequestDTO;
import com.althaf.employeemanagement.dto.EmployeeResponseDTO;
import com.althaf.employeemanagement.entity.Employee;
import com.althaf.employeemanagement.enums.EmployeeStatus;
import com.althaf.employeemanagement.exception.ResourceNotFoundException;
import com.althaf.employeemanagement.repository.EmployeeRepository;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequestDTO requestDTO;

    @BeforeEach
    void setUp(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("Clark")
                .email("tom@example.com")
                .department("IT")
                .salary(60000.0)
                .status(EmployeeStatus.ACTIVE)
                .build();

        requestDTO = EmployeeRequestDTO.builder()
                .firstName("Tom")
                .lastName("Clark")
                .email("tom@example.com")
                .department("IT")
                .salary(60000.0)
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    @Test
    void createEmployee_ShouldReturnResponseDTO()
    {

        when(employeeRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponseDTO response = employeeService.createEmployee(requestDTO);

        assertNotNull(response);
        assertEquals("Tom", response.getFirstName());
        assertEquals(EmployeeStatus.ACTIVE, response.getStatus());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenIdExists()
    {

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponseDTO response = employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("tom@example.com", response.getEmail());
    }

    @Test
    void getEmployeeById_ShouldThrowException_WhenIdDoesNotExist()
    {

        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(99L);
        });
    }
}

