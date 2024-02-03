package com.aoo.springboot.springbootrevision.service;

import com.aoo.springboot.springbootrevision.dto.EmployeeRequest;
import com.aoo.springboot.springbootrevision.dto.EmployeeResponse;
import com.aoo.springboot.springbootrevision.entities.Employee;
import com.aoo.springboot.springbootrevision.exceptions.EmailAlreadyExistsException;
import com.aoo.springboot.springbootrevision.respositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;



@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest employeeRequest;
    //@BeforeEach is used to signal that the annotated method should be executed before each @Test method in the current test class.


    @BeforeEach
    public void setup(){
        employeeRequest = EmployeeRequest.builder()
                .firstName("Elmi")
                .lastName("Robleh")
                .emailId("elmirobleh@gmail.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.existsByEmailId(employeeRequest.getEmailId()))
                .willReturn(false);

        given(employeeRepository.save(mapToEmployee(employeeRequest)))
                .willReturn(mapToEmployee(employeeRequest));

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when -  action or the behaviour that we are going test
        EmployeeResponse employeeResponse=employeeService.saveEmployee(employeeRequest);
        // then - verify the output
        assertThat(employeeResponse).isNotNull();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        // Setting up a mock scenario:
        // If the repository has an employee with the same email as in the request,
        // pretend that it's found and return it.
        given(employeeRepository.existsByEmailId(employeeRequest.getEmailId()))
                .willReturn(true);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when -  action or the behaviour that we are going test
        // Executing the action we want to test:
        // When we try to save an employee with an existing email,
        // it should throw a ResourceNotFoundException.
        org.junit.jupiter.api.Assertions.assertThrows(EmailAlreadyExistsException.class,()->{
            employeeService.saveEmployee(employeeRequest);
        });

        // then
        // Verifying that certain interactions happened:
        // We expect that the save method in the repository is never called.
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        // given - precondition or setup
        EmployeeRequest employeeRequest1 = EmployeeRequest.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiomar@gmail.com")
                .build();

        given(employeeRepository.findAll())
                .willReturn(List.of(mapToEmployee(employeeRequest),mapToEmployee(employeeRequest1)));

        // when -  action or the behaviour that we are going test

        List<EmployeeResponse> employeeResponses = employeeService.getAllEmployees();

        // then - verify the output

        assertThat(employeeResponses).isNotNull();
        assertThat(employeeResponses.size()).isEqualTo(2);
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given - precondition or setup
        EmployeeRequest employeeRequest1 = EmployeeRequest.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiomar@gmail.com")
                .build();
        given(employeeRepository.findAll())
                .willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<EmployeeResponse> employeeResponses = employeeService.getAllEmployees();

        // then - verify the output

        assertThat(employeeResponses).isEmpty();
        assertThat(employeeResponses.size()).isEqualTo(0);
    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(mapToEmployee(employeeRequest)));

        //when
        EmployeeResponse employeeResponse = employeeService.findEmployeeById(1L);

        //then
        assertThat(employeeResponse).isNotNull();
        assertThat(employeeResponse.getFirstName()).isEqualTo(employeeRequest.getFirstName());
        assertThat(employeeResponse.getLastName()).isEqualTo(employeeRequest.getLastName());
        assertThat(employeeResponse.getEmailId()).isEqualTo(employeeRequest.getEmailId());
    }

    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        EmployeeRequest employeeRequest1 = EmployeeRequest.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("abdiomar@gmail.com")
                .build();
        given(employeeRepository.findById(0L))
                .willReturn(Optional.of(mapToEmployee(employeeRequest)));
        given(employeeRepository.save(mapToEmployee(employeeRequest)))
                .willReturn(mapToEmployee(employeeRequest));
        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when -  action or the behaviour that we are going test
        EmployeeResponse employeeResponse = employeeService.updateEmployee(0L, employeeRequest1);
//        EmployeeResponse employeeResponse=employeeService.saveEmployee(employeeRequest);
        System.out.println(employeeResponse);
        // then - verify the ouput
        assertThat(employeeResponse.getFirstName()).isEqualTo(employeeRequest1.getFirstName());
        assertThat(employeeResponse.getLastName()).isEqualTo(employeeRequest1.getLastName());
        assertThat(employeeResponse.getEmailId()).isEqualTo(employeeRequest1.getEmailId());
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(mapToEmployee(employeeRequest)));
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when -  action or the behaviour that we are going test

        employeeService.deleteEmp(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
    public Employee mapToEmployee(EmployeeRequest employeeRequest){
        return Employee.builder()
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .emailId(employeeRequest.getEmailId())
                .build();
    }
}
