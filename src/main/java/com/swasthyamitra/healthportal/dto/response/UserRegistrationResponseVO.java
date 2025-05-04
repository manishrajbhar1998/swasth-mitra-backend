package com.swasthyamitra.healthportal.dto.response;


import lombok.Data;

import java.util.UUID;

@Data
public class UserRegistrationResponseVO {

    private UUID id;

    private String firstName;

    private String lastName;

    private Integer age;

    private String sex;

    private String maritalStatus;

    private String email;

    private String phoneNumber;

    private String address;

    private String pinCode;

    private String planSelection;

    private String patientHistory;

    private String existingDiseases;
}
