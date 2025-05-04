package com.swasthyamitra.healthportal.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserResponseVO {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String dateOfBirth;

    private String gender;

    private String address;

    private String role;
}
