package com.aoo.springboot.springbootrevision.controller;

import com.aoo.springboot.springbootrevision.dto.EmployeeRequest;
import com.aoo.springboot.springbootrevision.dto.EmployeeResponse;
import com.aoo.springboot.springbootrevision.entities.Employee;
import com.aoo.springboot.springbootrevision.exceptions.ResourceNotFoundException;
import com.aoo.springboot.springbootrevision.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Unit Testing with Mockito
@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    // we can mock also the employeeservice class

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        EmployeeRequest employee = EmployeeRequest.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiooa45@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(EmployeeRequest.class)))
//                .willAnswer((invocation) -> invocation.getArgument(0));
                .willReturn(mapToEmployeeResponse(employee));


        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.emailId",
                        is(employee.getEmailId())));
    }


    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        List<EmployeeRequest> listOfEmployees = new ArrayList<>();
        EmployeeRequest employee = EmployeeRequest.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiooa45@gmail.com")
                .build();
        listOfEmployees.add(employee);

        listOfEmployees.add(
                EmployeeRequest.builder()
                        .firstName("Dadinos")
                        .lastName("Ousleyeh")
                        .emailId("martelluiz@gmail.com").build()
        );
        List<EmployeeResponse> listOfEmployeeResponses = listOfEmployees.stream()
                .map(employee1 -> mapToEmployeeResponse(employee1))
                .collect(Collectors.toList());
//        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
        given(employeeService.getAllEmployees()).willReturn(listOfEmployeeResponses);
        // when -  action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(get("/api/v1/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));
    }

    // positive scenario - valid employee id
    // JUnit test for GET employee by id REST API
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        EmployeeRequest employee = EmployeeRequest.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiooa45@gmail.com")
                .build();

        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.of(mapToEmployeeResponse(employee)).get());

        // when -  action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.emailId",
                        is(employee.getEmailId())));
    }

    // negative scenario - valid employee id
    // JUnit test for GET employee by id REST API
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiooa45@gmail.com")
                .build();


        Mockito.when(employeeService.findEmployeeById(employeeId))
                .thenThrow(new ResourceNotFoundException());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for update employee REST API - positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiooa45@gmail.com")
                .build();

        // given - precondition or setup

        EmployeeRequest updatedEmployee = EmployeeRequest.builder()
                .firstName("Elmi")
                .lastName("Robleh")
                .emailId("elmirobleh@gmail.com")
                .build();

        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.of(mapToEmEmployeeResponse(employee)).get());

        given(employeeService.updateEmployee(eq(employeeId), any(EmployeeRequest.class)))
//                .willAnswer((invocation)-> invocation.getArgument(0));
                .willReturn(Optional.of(mapToEmployeeResponse(updatedEmployee)).get());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(patch("/api/v1/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.emailId",
                        is(updatedEmployee.getEmailId())));
    }

    // JUnit test for update employee REST API - negative scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiooa45@gmail.com")
                .build();

        // given - precondition or setup

        EmployeeRequest updatedEmployee = EmployeeRequest.builder()
                .firstName("Elmi")
                .lastName("Robleh")
                .emailId("elmirobleh@gmail.com")
                .build();

        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.of(mapToEmEmployeeResponse(employee)).get());

        given(employeeService.updateEmployee(eq(employeeId), any(EmployeeRequest.class)))
//                .willAnswer((invocation)-> invocation.getArgument(0));
                .willThrow(new ResourceNotFoundException());


        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(patch("/api/v1/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    // JUnit test for delete employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmp(employeeId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    public EmployeeResponse mapToEmployeeResponse(EmployeeRequest employee){
        return EmployeeResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .emailId(employee.getEmailId())
                .build();
    }
    public EmployeeResponse mapToEmEmployeeResponse(Employee employee){
        return EmployeeResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .emailId(employee.getEmailId())
                .build();
    }
}
