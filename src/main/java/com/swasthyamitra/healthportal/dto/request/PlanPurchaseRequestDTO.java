package com.swasthyamitra.healthportal.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PlanPurchaseRequestDTO {

    private String pastDisease; // "yes" or "no"
    private String presentDisease; // "yes" or "no"
    private String pastDiseaseInput;
    private List<String> existingDiseases;
    private String presentDiseaseOther;
    private MultipartFile profilePic;
}
