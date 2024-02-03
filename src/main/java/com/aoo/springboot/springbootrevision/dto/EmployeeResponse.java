package com.aoo.springboot.springbootrevision.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmployeeResponse {
    private String firstName;
    private String lastName;
    private String emailId;
}
