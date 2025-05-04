package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.dto.CommonDto;
import com.swasthyamitra.healthportal.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reference")
@RequiredArgsConstructor
public class ReferenceAPI {

    @GetMapping("/healthchecks")
    public CommonDto getHealthCheck() {
        try {
            return new CommonDto("Health Check Successful");
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
