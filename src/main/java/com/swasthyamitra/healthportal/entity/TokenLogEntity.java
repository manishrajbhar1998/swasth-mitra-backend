package com.swasthyamitra.healthportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "SM_TOKEN_LOG_ENTITY")
@Getter
@Setter
@NoArgsConstructor
public class TokenLogEntity extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "USER_ID")
    private UUID userId;

    @Column(name = "EMAIL", length = 256)
    private String email;

    @Column(name = "TOKEN", columnDefinition = "text")
    private String token;

    @Column(name = "ATTEMPT")
    private Integer attempt;

    //0-Invalid, 1-Valid, 2-Used
    @Column(name = "IS_VALID")
    private Integer isValid = 1;

    @Column(name = "EXPIRED_AT")
    private OffsetDateTime expiredAt;

    @Column(name = "MOBILE", length = 64)
    private String mobile;

}
