package com.swasthyamitra.healthportal.dto.response;

import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseVO {
    private String accessToken;

    private String type = "Bearer";
    private String username;
    private String firstName;
    private String lastName;
    private UUID userId;
    private RoleEnum role;
    private String refreshToken;
    private Boolean inquiryDetails = false;
    private Boolean registeredUsers = false;
    private Boolean manageAdmin = false;
    private Boolean delayedEnquiries = false;
    private Boolean exportTableData = false;


    public JwtResponseVO(String accessToken, String refreshToken, UserInfoEntity userInfoEntity, RoleEnum role) {
        this.accessToken = accessToken;
        this.username = userInfoEntity.getUsername();
        this.userId = userInfoEntity.getId();
        this.role = role;
        this.refreshToken = refreshToken;
        this.firstName = userInfoEntity.getFirstName();
        this.lastName = userInfoEntity.getLastName();
        this.inquiryDetails = userInfoEntity.getInquiryDetails();
        this.registeredUsers = userInfoEntity.getRegisteredUsers();
        this.manageAdmin = userInfoEntity.getManageAdmin();
        this.delayedEnquiries = userInfoEntity.getDelayedEnquiries();
        this.exportTableData = userInfoEntity.getExportTableData();
    }
}

