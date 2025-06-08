package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.PlanPurchaseRequestDTO;
import com.swasthyamitra.healthportal.dto.response.PlanPurchaseResponseDTO;
import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;
import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;

import com.swasthyamitra.healthportal.exception.InvalidInputException;
import com.swasthyamitra.healthportal.exception.ResourceNotFoundException;
import com.swasthyamitra.healthportal.repository.PlanPurchaseRepository;
import com.swasthyamitra.healthportal.service.PlanPurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PlanPurchaseServiceImpl implements PlanPurchaseService {

    @Autowired
    private PlanPurchaseRepository planPurchaseRepository;

    @Override
    public PlanPurchaseResponseDTO savePlanPurchase(PlanPurchaseRequestDTO requestDTO, UUID userId) throws IOException {

        if (planPurchaseRepository.findByUserId(userId).isPresent()) {
            throw new InvalidInputException("An individual plan already exists for this user.");
        }

        PlanPurchaseEntity planPurchaseEntity = mapper.convertPlanPurchaseRequesToPlanPurchaseEntity(requestDTO);
        planPurchaseEntity.setUserId(userId);
        planPurchaseEntity.setProfilePic(
                requestDTO.getProfilePic() != null && !requestDTO.getProfilePic().isEmpty()
                        ? requestDTO.getProfilePic().getBytes()
                        : null
        );
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
        updatedEntity.setUserId(userId);

        // Only update image if new file is provided
        if (requestDTO.getProfilePic() != null && !requestDTO.getProfilePic().isEmpty()) {
            updatedEntity.setProfilePic(requestDTO.getProfilePic().getBytes());
        } else {
            updatedEntity.setProfilePic(existingPlan.getProfilePic());
        }

        planPurchaseRepository.save(updatedEntity);

        return mapper.convertPlanPurchaseEntityToPlanPurchaseResponseDTO(updatedEntity);
    }


}
