package com.swasthyamitra.healthportal.dto.response;


import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseVO {

    private UUID id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String role;

    private String gender;

    private String maritalStatus;

    private String email;

    private String phoneNumber;

    private String address;

    private String pinCode;

    private String city;

    private String state;

    private String district;

    private String memberId;

    private Boolean inquiryDetails;

    private Boolean registeredUsers;

    private Boolean manageAdmin;

    private Boolean delayedEnquiries;

    private Boolean exportTableData;

    private Boolean createUser;

    private String plan;

    private String planExpiryDate;

    private String paymentStatus = "Plan Not Purchased";

    private String status = "IN_ACTIVE";

    private String createdAt;

    private String updatedAt;

    private String createdBy;

    private String updatedBy;
}
