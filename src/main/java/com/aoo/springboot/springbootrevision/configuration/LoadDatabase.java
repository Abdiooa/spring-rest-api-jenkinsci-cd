package com.aoo.springboot.springbootrevision.configuration;

import com.aoo.springboot.springbootrevision.entities.Employee;
import com.aoo.springboot.springbootrevision.respositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository){
        Employee employee1 = Employee.builder()
                .firstName("Abdi")
                .lastName("Omar")
                .emailId("martelluiz125@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Aoo")
                .lastName("Ousleyeh")
                .emailId("abdiooa45@gmail.com")
                .build();

        return args -> {
            log.info("Preloading "+employeeRepository.save(employee1));
            log.info("Preloading "+employeeRepository.save(employee2));
        };
    }
}
