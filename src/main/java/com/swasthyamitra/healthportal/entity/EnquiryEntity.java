package com.swasthyamitra.healthportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "SM_ENQUIRY")
@Data
public class EnquiryEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "MOBILE_NO", nullable = false)
    private String mobileNo;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "STATE")
    private String state;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "PIN_CODE")
    private String pinCode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "USER_MSG")
    private String userMsg;

    @Column(name = "IS_EDITABLE")
    private boolean isEditable = true;
}

