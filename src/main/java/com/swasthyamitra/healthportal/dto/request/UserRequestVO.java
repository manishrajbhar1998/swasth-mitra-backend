package com.swasthyamitra.healthportal.dto.request;

import lombok.Data;

@Data
public class UserRequestVO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String dateOfBirth;

    private String gender;

    private String address;

    private String role;
}
