package com.swasthyamitra.healthportal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "SM_USER_REGISTRATION")
public class UserRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "AGE", nullable = false)
    private Integer age;

    @Column(name = "MARITAL_STATUS", nullable = false)
    private String maritalStatus;

    @Column(name = "PLAN_SELECTION", nullable = false)
    private String planSelection;

    @Column(name = "PATIENT_HISTORY")
    private String patientHistory;

    @Column(name = "EXISTING_DISEASES", nullable = false)
    private String existingDiseases;

    @Column(name = "USER_INFO_ID")
    private UUID userInfoId;

    // Establish the one-to-one relationship
    @OneToOne
    @JoinColumn(name = "USER_INFO_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private UserInfoEntity userInfoEntity;


}
