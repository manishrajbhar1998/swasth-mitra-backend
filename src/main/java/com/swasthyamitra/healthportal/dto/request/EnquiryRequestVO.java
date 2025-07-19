package com.swasthyamitra.healthportal.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class EnquiryRequestVO
{

    @NotBlank(message = "User name is required and cannot be blank.")
    private String userName;

    @NotBlank(message = "Email address is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Mobile number is required.")
    private String mobileNo;

    private String address;

    private String city;
    private String district;
    private String state;
    private String country;
    private String pinCode;
    private String createdBy;
    private String updatedBy;
    private UUID followUpUUID;
    private String status;
    private String userMsg;

}
