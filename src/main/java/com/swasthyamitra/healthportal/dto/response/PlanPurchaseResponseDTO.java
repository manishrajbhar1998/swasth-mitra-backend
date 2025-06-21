package com.swasthyamitra.healthportal.dto.response;

import com.swasthyamitra.healthportal.dto.request.FamilyMembersDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PlanPurchaseResponseDTO {

    private UUID id;
    private String plan;
    private String memberId;
    private String pastDisease;
    private String presentDisease;
    private String pastDiseaseInput;
    private List<String> existingDiseases;
    private String presentDiseaseOther;
    private String profilePic;
    private String createdAt;
    private String planExpiryDate;
    private String paymentStatus;
    private Double amount;
    private String status;
    private String anyChild;
    private Integer numofChild;
    private FamilyMembersDTO familyMembersDTO;

}
