package com.aoo.springboot.springbootrevision.controller;

import com.aoo.springboot.springbootrevision.dto.EmployeeRequest;
import com.aoo.springboot.springbootrevision.dto.EmployeeResponse;
import com.aoo.springboot.springbootrevision.exceptions.EmailAlreadyExistsException;
import com.aoo.springboot.springbootrevision.exceptions.ResourceNotFoundException;
import com.aoo.springboot.springbootrevision.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequest employeeRequest){
        try {
            EmployeeResponse employeeResponse = employeeService.saveEmployee(employeeRequest);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(employeeResponse,headers, HttpStatus.CREATED);
        }catch (EmailAlreadyExistsException emailAlreadyExistsException){
            return new ResponseEntity<>("Email already used by other employee we sorry",HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            return new ResponseEntity<>("An error occurred while processing the request.",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getAllEmployees(){
        try {
            List<EmployeeResponse> employeeResponses = employeeService.getAllEmployees();
            return  new ResponseEntity<>(employeeResponses, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("An error occurred while processing the request.",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") long id){
        try {
            EmployeeResponse employeeResponse = employeeService.findEmployeeById(id);
            return new ResponseEntity<>(employeeResponse,HttpStatus.OK);
        }catch (ResourceNotFoundException resourceNotFoundException){
            return new ResponseEntity<>("An employee with this identifiant does not exists",HttpStatus.NOT_FOUND);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") long id, @RequestBody EmployeeRequest employeeRequest){
        try{
            EmployeeResponse employeeResponse = employeeService.updateEmployee(id,employeeRequest);
            return new ResponseEntity<>(employeeResponse,HttpStatus.OK);
        }catch (ResourceNotFoundException resourceNotFoundException){
            return new ResponseEntity<>("An employee with this identifiant does not exists",HttpStatus.NOT_FOUND);
        }
        catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id){
        try{
            employeeService.deleteEmp(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (ResourceNotFoundException resourceNotFoundException){
            return new ResponseEntity<>("An employee with this identifiant does not exists",HttpStatus.NOT_FOUND);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
