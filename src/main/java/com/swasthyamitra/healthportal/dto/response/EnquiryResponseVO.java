package com.swasthyamitra.healthportal.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class EnquiryResponseVO  {

    private UUID id;
    private String userName;
    private String email;
    private String mobileNo;
    private String address;
    private String city;
    private String district;
    private String state;
    private String country;
    private String pinCode;
}
