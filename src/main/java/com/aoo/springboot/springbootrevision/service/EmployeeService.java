package com.aoo.springboot.springbootrevision.service;

import com.aoo.springboot.springbootrevision.dto.EmployeeRequest;
import com.aoo.springboot.springbootrevision.dto.EmployeeResponse;
import com.aoo.springboot.springbootrevision.exceptions.EmailAlreadyExistsException;
import com.aoo.springboot.springbootrevision.exceptions.ResourceNotFoundException;

import java.util.List;

public interface EmployeeService {
    public List<EmployeeResponse> getAllEmployees();
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) throws EmailAlreadyExistsException;

    public EmployeeResponse findEmployeeById(long employeeId) throws ResourceNotFoundException;

    public void deleteEmp(long employeeId) throws ResourceNotFoundException;

    public EmployeeResponse updateEmployee(long employeeId, EmployeeRequest employeeRequest) throws ResourceNotFoundException;
}
