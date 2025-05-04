package com.swasthyamitra.healthportal.entity;

import com.swasthyamitra.healthportal.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "SM_USER_TOKEN")
public class TokenEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public UUID id;

    @Column(name = "JWT_TOKEN", columnDefinition = "TEXT")
    public String jwtToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "TOEKN_TYPE")
    public TokenType tokenType = TokenType.BEARER;

    @Column(name = "REVOKED")
    public boolean revoked;

    @Column(name = "EXPIRED")
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public UserInfoEntity userInfo;
}
