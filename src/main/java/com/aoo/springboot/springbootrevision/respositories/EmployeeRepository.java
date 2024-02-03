package com.aoo.springboot.springbootrevision.respositories;

import com.aoo.springboot.springbootrevision.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailId(String EmailId);

    Optional<Employee> findById(long employeeId);

    Optional<Employee> findByEmailId(String emailId);
}
