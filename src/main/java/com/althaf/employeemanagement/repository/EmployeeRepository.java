package com.althaf.employeemanagement.repository;

import com.althaf.employeemanagement.entity.Employee;
import com.althaf.employeemanagement.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

        boolean existsByEmail(String email);
        boolean existsByEmailAndIdNot(String email, Long id);

        Page<Employee> findByFirstNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(String firstName, String department, Pageable pageable);

        Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);
}
