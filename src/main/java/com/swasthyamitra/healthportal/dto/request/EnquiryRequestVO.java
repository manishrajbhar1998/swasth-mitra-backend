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

    private String email;

    @NotBlank(message = "Mobile number is required.")
    private String mobileNo;

    private String address;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "District is required.")
    private String district;

    @NotBlank(message = "State is required.")
    private String state;
    private String country;
    private String pinCode;
    private String createdBy;
    private String updatedBy;
    private UUID followUpUUID;
    private String status;
    private String userMsg;

}
