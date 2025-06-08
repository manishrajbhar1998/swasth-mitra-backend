package com.swasthyamitra.healthportal.entity;

import com.swasthyamitra.healthportal.convertor.StringListConvertor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "SM_PLAN_PURCHASE")
public class PlanPurchaseEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "PAST_DISEASE")
    private String pastDisease;

    @Column(name = "PRESENT_DISEASE")
    private String presentDisease;

    @Column(name = "PAST_DISEASE_INPUT")
    private String pastDiseaseInput;

    @Column(name = "EXISTING_DISEASE", columnDefinition = "TEXT")
    @Convert(converter = StringListConvertor.class)
    private List<String> existingDiseases;

    @Column(name = "PRESENT_DISEASE_OTHER")
    private String presentDiseaseOther;

    @Lob
    @Column(name = "PROFILE_PIC", columnDefinition = "LONGBLOB")
    private byte[] profilePic;

    @Column(name = "USER_ID")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false,updatable = false)
    private UserInfoEntity user;

}
