package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.dto.request.PlanPurchaseRequestDTO;
import com.swasthyamitra.healthportal.dto.response.ApiResponse;
import com.swasthyamitra.healthportal.dto.response.PlanPurchaseResponseDTO;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.service.PlanPurchaseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/plan-purchase")
@Slf4j
public class PlanPurchaseController {

    @Autowired
    private PlanPurchaseService planPurchaseService;

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PlanPurchaseResponseDTO>> submitPlanPurchase(
            @Valid @ModelAttribute PlanPurchaseRequestDTO requestDTO) throws IOException {

        log.info("Received plan purchase submission.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoEntity userInfoEntity = (UserInfoEntity) authentication.getPrincipal();

        PlanPurchaseResponseDTO responseDTO = planPurchaseService.savePlanPurchase(requestDTO, userInfoEntity.getId());

        ApiResponse<PlanPurchaseResponseDTO> response = ApiResponse.<PlanPurchaseResponseDTO>builder()
                .data(responseDTO)
                .message("Plan purchase submitted successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PlanPurchaseResponseDTO>> getUserPlanPurchase() {

        log.info("Fetching plan purchase for logged-in user.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoEntity userInfoEntity = (UserInfoEntity) authentication.getPrincipal();

        PlanPurchaseResponseDTO responseDTO = planPurchaseService.getPlanPurchaseByUserId(userInfoEntity.getId());

        ApiResponse<PlanPurchaseResponseDTO> response = ApiResponse.<PlanPurchaseResponseDTO>builder()
                .data(responseDTO)
                .message("Plan purchase fetched successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PlanPurchaseResponseDTO>> updateUserPlanPurchase(
            @Valid @ModelAttribute PlanPurchaseRequestDTO requestDTO) throws IOException {

        log.info("Updating plan purchase for logged-in user.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoEntity userInfoEntity = (UserInfoEntity) authentication.getPrincipal();

        PlanPurchaseResponseDTO responseDTO = planPurchaseService.updatePlanPurchase(requestDTO, userInfoEntity.getId());

        ApiResponse<PlanPurchaseResponseDTO> response = ApiResponse.<PlanPurchaseResponseDTO>builder()
                .data(responseDTO)
                .message("Plan purchase updated successfully.")
                .build();

        return ResponseEntity.ok(response);
    }


}
