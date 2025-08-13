package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.FamilyMemberDTO;
import com.swasthyamitra.healthportal.dto.request.FamilyMembersDTO;
import com.swasthyamitra.healthportal.dto.request.PlanPurchaseRequestDTO;
import com.swasthyamitra.healthportal.dto.response.PlanPurchaseResponseDTO;
import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;

import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;

import com.swasthyamitra.healthportal.exception.InvalidInputException;
import com.swasthyamitra.healthportal.exception.ResourceNotFoundException;
import com.swasthyamitra.healthportal.repository.PlanPurchaseRepository;
import com.swasthyamitra.healthportal.service.FileUploadService;
import com.swasthyamitra.healthportal.service.PlanPurchaseService;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PlanPurchaseServiceImpl implements PlanPurchaseService {

    @Autowired
    private PlanPurchaseRepository planPurchaseRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public PlanPurchaseResponseDTO savePlanPurchase(PlanPurchaseRequestDTO requestDTO, UUID userId) throws IOException {

        if (planPurchaseRepository.findByUserId(userId).isPresent()) {
            throw new InvalidInputException("plan already exists for this user.");
        }

        PlanPurchaseEntity planPurchaseEntity = mapper.convertPlanPurchaseRequesToPlanPurchaseEntity(requestDTO);
        planPurchaseEntity.setUserId(userId);
        planPurchaseEntity.setMemberId(CommonUtils.generateMemberId());
        planPurchaseEntity.setProfilePic(
                requestDTO.getProfilePic() != null && !requestDTO.getProfilePic().isEmpty()
                        ? convertImageUrlToBase64(fileUploadService.storeFile(requestDTO.getProfilePic()))
                        : null
        );

        FamilyMembersDTO updatedFamilyMembers = processFamilyMemberPhotos(requestDTO.getFamilyMembersDTO());
        planPurchaseEntity.setFamilyMembersDTO(updatedFamilyMembers);
        planPurchaseEntity.setStatus("IN_ACTIVE");

        if (requestDTO.getPaymentStatus().equalsIgnoreCase("SUCCESS")) {
            planPurchaseEntity.setPlanExpiryDate(calculatePlanExpiryDate());
        }

        planPurchaseRepository.save(planPurchaseEntity);

        return mapper.convertPlanPurchaseEntityToPlanPurchaseResponseDTO(planPurchaseEntity);
    }

    @Override
    public PlanPurchaseResponseDTO getPlanPurchaseByUserId(UUID userId) {
        PlanPurchaseEntity planPurchaseEntity = planPurchaseRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No plan purchase found for this user."));

        return mapper.convertPlanPurchaseEntityToPlanPurchaseResponseDTO(planPurchaseEntity);
    }

    @Override
    public PlanPurchaseResponseDTO updatePlanPurchase(PlanPurchaseRequestDTO requestDTO, UUID userId) throws IOException {

        PlanPurchaseEntity existingPlan = planPurchaseRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No plan found to update for this user."));

        PlanPurchaseEntity updatedEntity = mapper.convertPlanPurchaseRequesToPlanPurchaseEntity(requestDTO);

        // Preserve entity ID and userId
        updatedEntity.setId(existingPlan.getId());
        updatedEntity.setMemberId(existingPlan.getMemberId());
        updatedEntity.setUserId(userId);
        updatedEntity.setStatus("IN_ACTIVE");

        if (requestDTO.getPaymentStatus().equalsIgnoreCase("SUCCESS")) {
            updatedEntity.setPlanExpiryDate(calculatePlanExpiryDate());
        }

        // Only update image if new file is provided
        if (requestDTO.getProfilePic() != null && !requestDTO.getProfilePic().isEmpty()) {
            updatedEntity.setProfilePic(convertImageUrlToBase64(fileUploadService.storeFile(requestDTO.getProfilePic())));
        } else {
            updatedEntity.setProfilePic(existingPlan.getProfilePic());
        }

        FamilyMembersDTO updatedFamilyMembers = processFamilyMemberPhotos(requestDTO.getFamilyMembersDTO());
        updatedEntity.setFamilyMembersDTO(updatedFamilyMembers);

        planPurchaseRepository.save(updatedEntity);

        return mapper.convertPlanPurchaseEntityToPlanPurchaseResponseDTO(updatedEntity);
    }

    @Override
    public List<PlanPurchaseResponseDTO> getPlanPurchaseByMemberId(String memberId) {

        List<PlanPurchaseEntity> planPurchaseEntities;

        if(memberId != null && !memberId.isEmpty()){
             planPurchaseEntities = planPurchaseRepository.findByMemberId(memberId);
        }else {
            planPurchaseEntities = planPurchaseRepository.findAll();
        }

        return planPurchaseEntities.stream()
                .map(entity -> {
                    PlanPurchaseResponseDTO dto = mapper.convertPlanPurchaseEntityToPlanPurchaseResponseDTO(entity);
                    dto.setProfilePic(convertImageUrlToBase64(entity.getProfilePic()));
                    return dto;
                })
                .toList();    }

    private Timestamp calculatePlanExpiryDate() {
        LocalDateTime expiryDateTime = LocalDateTime.now()
                .plusYears(1)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);

        return Timestamp.valueOf(expiryDateTime);
    }

    private FamilyMembersDTO processFamilyMemberPhotos(FamilyMembersDTO familyMembers) {
        if (familyMembers == null) return null;

        // Handle spouse
        if (familyMembers.getSpouse() != null &&
                familyMembers.getSpouse().getProfilePic() != null &&
                !familyMembers.getSpouse().getProfilePic().isEmpty()) {

            String url = fileUploadService.storeFile(familyMembers.getSpouse().getProfilePic());
            familyMembers.getSpouse().setProfilePhotoUrl(convertImageUrlToBase64(url));
        }

        // Handle father
        if (familyMembers.getFather() != null &&
                familyMembers.getFather().getProfilePic() != null &&
                !familyMembers.getFather().getProfilePic().isEmpty()) {

            String url = fileUploadService.storeFile(familyMembers.getFather().getProfilePic());
            familyMembers.getFather().setProfilePhotoUrl(convertImageUrlToBase64(url));
        }

        // Handle mother
        if (familyMembers.getMother() != null &&
                familyMembers.getMother().getProfilePic() != null &&
                !familyMembers.getMother().getProfilePic().isEmpty()) {

            String url = fileUploadService.storeFile(familyMembers.getMother().getProfilePic());
            familyMembers.getMother().setProfilePhotoUrl(convertImageUrlToBase64(url));
        }

        // Handle children
        if (familyMembers.getChildren() != null) {
            for (FamilyMemberDTO child : familyMembers.getChildren()) {
                if (child.getProfilePic() != null && !child.getProfilePic().isEmpty()) {
                    String url = fileUploadService.storeFile(child.getProfilePic());
                    child.setProfilePhotoUrl(convertImageUrlToBase64(url));
                }
            }
        }

        return familyMembers;
    }
    public  String convertImageUrlToBase64(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return null;

        try {
            byte[] imageBytes = fileUploadService.downloadImageAsBytes(imageUrl);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            // Log or handle gracefully
            return null;
        }
    }

}
