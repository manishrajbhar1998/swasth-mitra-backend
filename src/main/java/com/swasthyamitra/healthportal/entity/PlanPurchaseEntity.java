package com.swasthyamitra.healthportal.entity;

import com.swasthyamitra.healthportal.convertor.FamilyMembersConverter;
import com.swasthyamitra.healthportal.convertor.StringListConvertor;
import com.swasthyamitra.healthportal.dto.request.FamilyMembersDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
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

    @Column(name = "PLAN")
    private String plan;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "PRESENT_DISEASE")
    private String presentDisease;

    @Column(name = "PAST_DISEASE_INPUT")
    private String pastDiseaseInput;

    @Column(name = "EXISTING_DISEASE", columnDefinition = "TEXT")
    @Convert(converter = StringListConvertor.class)
    private List<String> existingDiseases;

    @Column(name = "PRESENT_DISEASE_OTHER")
    private String presentDiseaseOther;

    @Column(name = "PROFILE_PIC")
    private String profilePic;

    @Column(name = "USER_ID")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false,updatable = false)
    private UserInfoEntity user;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "PLAN_EXPIRY_DATE")
    private Timestamp planExpiryDate;

    @Column(name = "STATUS")
    private String status = "IN_ACTIVE";

    @Column(name = "NUM_OF_CHIELD")
    private Integer numofChild;

    @Column(name = "ANY_CHIELD")
    private String anyChild;

    @Lob
    @Column(name = "FAMILY_MEMBERS", columnDefinition = "TEXT")
    @Convert(converter = FamilyMembersConverter.class)
    private FamilyMembersDTO familyMembersDTO;

}
