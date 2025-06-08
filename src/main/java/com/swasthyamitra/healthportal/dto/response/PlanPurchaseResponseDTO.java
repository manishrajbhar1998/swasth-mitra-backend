package com.swasthyamitra.healthportal.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PlanPurchaseResponseDTO {

    private UUID id;
    private String pastDisease;
    private String presentDisease;
    private String pastDiseaseInput;
    private List<String> existingDiseases;
    private String presentDiseaseOther;
    private Byte[] profilePic;
    private String createdAt;
}
