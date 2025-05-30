package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.response.ApiResponse;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.service.EnquiryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enquiry")
@Slf4j
public class EnquiryAPI {

    private final EnquiryService enquiryService;

    @Autowired
    public EnquiryAPI(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<EnquiryResponseVO>> submitEnquiry(@Valid @RequestBody EnquiryRequestVO enquiryRequest) {
        log.info("Received enquiry submission from user: {}", enquiryRequest.getUserName());

        EnquiryResponseVO enquiryResponse = enquiryService.saveEnquiry(enquiryRequest);

        ApiResponse<EnquiryResponseVO> response = ApiResponse.<EnquiryResponseVO>builder()
                .data(enquiryResponse)
                .message("Enquiry submitted successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<EnquiryResponseVO>>> getAllEnquiries() {
        log.info("Fetching all enquiries...");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoEntity userInfoEntity = (UserInfoEntity) authentication.getPrincipal();

        List<EnquiryResponseVO> enquiries = enquiryService.getAllEnquiries(userInfoEntity.getRoleEnum(),userInfoEntity.getState(),
                userInfoEntity.getDistrict());

        ApiResponse<List<EnquiryResponseVO>> response = ApiResponse.<List<EnquiryResponseVO>>builder()
                .data(enquiries)
                .message("Enquiries fetched successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<EnquiryResponseVO>> updateEnquiry(
            @PathVariable("id") UUID enquiryId,
            @Valid @RequestBody EnquiryRequestVO enquiryRequest) {

        log.info("Updating enquiry with ID: {}", enquiryId);

        EnquiryResponseVO updatedResponse = enquiryService.updateEnquiry(enquiryId, enquiryRequest);

        ApiResponse<EnquiryResponseVO> response = ApiResponse.<EnquiryResponseVO>builder()
                .data(updatedResponse)
                .message("Enquiry updated successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<EnquiryResponseVO>> getEnquiryById(@PathVariable("id") UUID enquiryId) {

        log.info("Fetching enquiry with ID: {}", enquiryId);

        EnquiryResponseVO enquiryResponse = enquiryService.getEnquiryById(enquiryId);

        ApiResponse<EnquiryResponseVO> response = ApiResponse.<EnquiryResponseVO>builder()
                .data(enquiryResponse)
                .message("Enquiry fetched successfully.")
                .build();

        return ResponseEntity.ok(response);
    }


}
