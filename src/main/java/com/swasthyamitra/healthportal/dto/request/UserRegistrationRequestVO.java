package com.swasthyamitra.healthportal.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequestVO {

    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @NotNull(message = "Age is required.")
    private Integer age;

    @NotBlank(message = "Sex is required.")
    private String sex;

    @NotBlank(message = "Marital Status is required.")
    private String maritalStatus;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Phone Number is required.")
    private String phoneNumber;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Pin code is required.")
    private String pinCode;

    @NotBlank(message = "Plan Selection is required.")
    private String planSelection;

    private String patientHistory;

    @NotBlank(message = "Existing Diseases selection is required.")
    private String existingDiseases;
}
