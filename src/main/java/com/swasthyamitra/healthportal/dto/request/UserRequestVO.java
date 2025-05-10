package com.swasthyamitra.healthportal.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestVO {

    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @NotNull(message = "Age is required.")
    private Integer age;

    @NotBlank(message = "Gender is required.")
    private String gender;

    private String maritalStatus;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Phone Number is required.")
    private String phoneNumber;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Role is required.")
    private String role;

    @NotBlank(message = "Pin code is required.")
    private String pinCode;

    private String planSelection;

    private String patientHistory;

    private String existingDiseases;
}
