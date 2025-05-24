package com.swasthyamitra.healthportal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "SM_USER_INFO")
public class UserInfoEntity extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "PIN_CODE")
    private String pinCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date dateOfBirth;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ENCODED_PASSWORD", nullable = false)
    private String encodedPassword;

    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @Column(name = "MARITAL_STATUS", nullable = false)
    private String maritalStatus;

    @Column(name = "PLAN_SELECTION", nullable = false)
    private String planSelection;

    @Column(name = "PATIENT_HISTORY")
    private String patientHistory;

    @Column(name = "EXISTING_DISEASES", nullable = false)
    private String existingDiseases;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "DISTRICT")
    private String district;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleEnum.name()));
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

