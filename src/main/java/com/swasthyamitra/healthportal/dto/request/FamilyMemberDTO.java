package com.swasthyamitra.healthportal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FamilyMemberDTO {

    private String name;
    private String dob;
    private String presentDisease;
    private String pastDiseaseInput;
    private List<String> existingDiseases;
    private String presentDiseaseOther;
    @JsonIgnore
    private transient MultipartFile profilePic;
    private String profilePhotoUrl;
}
