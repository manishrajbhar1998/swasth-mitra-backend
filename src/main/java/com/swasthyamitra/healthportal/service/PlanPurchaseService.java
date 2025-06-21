package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.dto.request.PlanPurchaseRequestDTO;
import com.swasthyamitra.healthportal.dto.response.PlanPurchaseResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PlanPurchaseService {
    PlanPurchaseResponseDTO savePlanPurchase(PlanPurchaseRequestDTO requestDTO, UUID id) throws IOException;

    PlanPurchaseResponseDTO getPlanPurchaseByUserId(UUID id);

    PlanPurchaseResponseDTO updatePlanPurchase(PlanPurchaseRequestDTO requestDTO, UUID id) throws IOException;

    List<PlanPurchaseResponseDTO> getPlanPurchaseByMemberId(String memberId);
}
