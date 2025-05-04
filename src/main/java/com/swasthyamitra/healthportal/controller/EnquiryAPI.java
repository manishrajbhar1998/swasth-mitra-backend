package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.constants.ApiResponseMessages;
import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.request.LoginRequestVO;
import com.swasthyamitra.healthportal.dto.response.ApiResponse;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.dto.response.JwtResponseVO;
import com.swasthyamitra.healthportal.service.EnquiryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        List<EnquiryResponseVO> enquiries = enquiryService.getAllEnquiries();

        ApiResponse<List<EnquiryResponseVO>> response = ApiResponse.<List<EnquiryResponseVO>>builder()
                .data(enquiries)
                .message("Enquiries fetched successfully.")
                .build();

        return ResponseEntity.ok(response);
    }


}
