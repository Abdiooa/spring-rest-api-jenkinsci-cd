package com.aoo.springboot.springbootrevision.service;


import com.aoo.springboot.springbootrevision.dto.EmployeeRequest;
import com.aoo.springboot.springbootrevision.dto.EmployeeResponse;
import com.aoo.springboot.springbootrevision.entities.Employee;
import com.aoo.springboot.springbootrevision.exceptions.EmailAlreadyExistsException;
import com.aoo.springboot.springbootrevision.exceptions.ResourceNotFoundException;
import com.aoo.springboot.springbootrevision.respositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    private  final EmployeeRepository employeeRepository;
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::mapToEmployeeResponse).collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) throws EmailAlreadyExistsException {
        if(employeeRepository.existsByEmailId(employeeRequest.getEmailId())){
            throw new EmailAlreadyExistsException();
        }

        validateInput(employeeRequest);
        Employee employee = mapToEmployee(employeeRequest);
        employeeRepository.save(employee);
        log.info(" Employee {} saved successfully.",employee.getId());

        return mapToEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse findEmployeeById(long employeeId) throws ResourceNotFoundException {
        if(employeeRepository.findById(employeeId).isEmpty()){
            throw new ResourceNotFoundException();
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new IllegalArgumentException("Employee with the id "+ employeeId+ " not found");
        }

        return mapToEmployeeResponse(optionalEmployee.get());
    }

    @Override
    public void deleteEmp(long employeeId) throws ResourceNotFoundException {
        if(employeeRepository.findById(employeeId).isEmpty()){
            throw new ResourceNotFoundException();
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new IllegalArgumentException("Employee with the id "+ employeeId+ " not found");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public EmployeeResponse updateEmployee(long employeeId, EmployeeRequest employeeRequest) throws ResourceNotFoundException {
        if(employeeRepository.findById(employeeId).isEmpty()){
            throw new ResourceNotFoundException();
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new IllegalArgumentException("Employee with the id "+ employeeId+ " not found");
        }
        if(employeeRequest.getFirstName()!=null && !employeeRequest.getFirstName().isEmpty()){
            optionalEmployee.get().setFirstName(employeeRequest.getFirstName());
        }
        if(employeeRequest.getLastName()!=null && !employeeRequest.getLastName().isEmpty()){
            optionalEmployee.get().setLastName(employeeRequest.getLastName());
        }
        if(employeeRequest.getEmailId()!=null && !employeeRequest.getEmailId().isEmpty()){
            optionalEmployee.get().setEmailId(employeeRequest.getEmailId());
        }
        employeeRepository.save(optionalEmployee.get());
        log.info("Person {} updated succefully.",optionalEmployee.get().getId());
        return mapToEmployeeResponse(optionalEmployee.get());
    }

    public EmployeeResponse mapToEmployeeResponse(Employee employee){
        return EmployeeResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .emailId(employee.getEmailId())
                .build();
    }

    public void validateInput(EmployeeRequest employeeRequest){
        if(employeeRequest.getFirstName() == null || employeeRequest.getLastName() == null ||
                 employeeRequest.getEmailId() == null){
            throw new IllegalArgumentException(
                    "Invalid input data."
            );
        }
    }

    public Employee mapToEmployee(EmployeeRequest employeeRequest){
        return Employee.builder()
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .emailId(employeeRequest.getEmailId())
                .build();
    }

}
