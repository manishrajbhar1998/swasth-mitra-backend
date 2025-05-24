package com.swasthyamitra.healthportal.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestVO {

    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @NotBlank(message = "Date of Birth is required.")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Date of Birth must be in the format dd-MM-yyyy")
    private String dateOfBirth;

    @NotBlank(message = "Gender is required.")
    private String gender;

    private String maritalStatus;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Phone Number is required.")
    private String phoneNumber;

    @NotBlank(message = "Address Number is required.")
    private String address;

    @NotBlank(message = "Role is required.")
    private String role;

    @NotBlank(message = "Pin code is required.")
    private String pinCode;

    private String planSelection;

    private String patientHistory;

    private String existingDiseases;

    @NotBlank(message = "Password is required.")
    private String password;

    private String createdBy;

    private String updatedBy;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "State is required.")
    private String state;
}
