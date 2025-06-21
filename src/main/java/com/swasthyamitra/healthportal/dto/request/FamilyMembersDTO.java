package com.swasthyamitra.healthportal.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class FamilyMembersDTO {

    private FamilyMemberDTO spouse;
    private FamilyMemberDTO father;
    private FamilyMemberDTO mother;
    private List<FamilyMemberDTO> children;
}
