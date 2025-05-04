package com.swasthyamitra.healthportal.dto.response;

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
    private UUID userId;
    private RoleEnum role;

    private String refreshToken;

    public JwtResponseVO(String accessToken, String refreshToken, String username, UUID userId, RoleEnum role) {
        this.accessToken = accessToken;
        this.username = username;
        this.userId = userId;
        this.role = role;
        this.refreshToken = refreshToken;
    }
}

