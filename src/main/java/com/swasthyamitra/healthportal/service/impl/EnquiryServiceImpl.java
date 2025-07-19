package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.entity.EnquiryEntity;

import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;

import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.exception.ResourceNotFoundException;
import com.swasthyamitra.healthportal.repository.EnquiryRepository;
import com.swasthyamitra.healthportal.service.EnquiryService;
import com.swasthyamitra.healthportal.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;

    @Autowired
    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    public EnquiryResponseVO saveEnquiry(EnquiryRequestVO enquiryRequest) {
        log.info("Saving new enquiry for user: {}", enquiryRequest.getUserName());
        ValidationUtils.Cc(enquiryRequest);

        EnquiryEntity enquiryEntity = mapper.convertEnquiryRequestToEnquiryEntity(enquiryRequest);
        enquiryEntity.setCreatedBy(enquiryRequest.getCreatedBy());
        enquiryEntity.setCreatedByUUID(enquiryRequest.getFollowUpUUID());
        EnquiryEntity savedEnquiry = enquiryRepository.save(enquiryEntity);

        log.info("Enquiry saved successfully with ID: {}", savedEnquiry.getId());
        return mapper.convertEnquiryEntityToEnquiryResponse(savedEnquiry);
    }

    @Override
    public List<EnquiryResponseVO> getAllEnquiries(RoleEnum role, String state, String district, String city ,Boolean delayedEnquiries) {
        log.info("Fetching all enquiries from the database...");

        List<EnquiryEntity> enquiryEntities;

        if ("SUPER_ADMIN".equalsIgnoreCase(role.toString())) {
            // No role filter — fetch all
            enquiryEntities = enquiryRepository.findAll();
        } else if ("STATE_ADMIN".equalsIgnoreCase(role.toString())) {
            enquiryEntities = enquiryRepository.findByStateIgnoreCase(state);
        } else if ("TEAM_LEADS".equalsIgnoreCase(role.toString())) {
            enquiryEntities = enquiryRepository.findByStateIgnoreCaseAndDistrictIgnoreCaseAndCityIgnoreCase(state, district, city);
        } else if ("USER".equalsIgnoreCase(role.toString())) {
            enquiryEntities = new ArrayList<>();
        } else {
            enquiryEntities = enquiryRepository.findByStateIgnoreCaseAndDistrictIgnoreCase(state, district);
        }

        // Apply delayed enquiries filter
        if (Boolean.TRUE.equals(delayedEnquiries)) {
            enquiryEntities = enquiryEntities.stream()
                    .filter(enquiry -> enquiry.getStatus() == null)
                    .collect(Collectors.toList());
        }

        log.info("Total enquiries fetched: {}", enquiryEntities.size());
        return enquiryEntities.stream()
                .map(mapper::convertEnquiryEntityToEnquiryResponse)
                .toList();
    }

    @Override
    public EnquiryResponseVO updateEnquiry(UUID enquiryId, EnquiryRequestVO enquiryRequest) {

        log.info("Updating enquiry with ID: {}", enquiryId);

        ValidationUtils.Cc(enquiryRequest);

        EnquiryEntity existingEntity = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found with ID: " + enquiryId));

        EnquiryEntity enquiryEntity = mapper.convertEnquiryRequestToEnquiryEntity(enquiryRequest);
        enquiryEntity.setCreatedBy(enquiryRequest.getCreatedBy());
        enquiryEntity.setCreatedByUUID(enquiryRequest.getFollowUpUUID());
        enquiryEntity.setId(existingEntity.getId());
        EnquiryEntity savedEnquiry = enquiryRepository.save(enquiryEntity);

        return mapper.convertEnquiryEntityToEnquiryResponse(savedEnquiry);
    }

    @Override
    public EnquiryResponseVO getEnquiryById(UUID enquiryId) {
        log.info("Fetching enquiry by ID: {}", enquiryId);

        EnquiryEntity enquiryEntity = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found with ID: " + enquiryId));

        return mapper.convertEnquiryEntityToEnquiryResponse(enquiryEntity);
    }

}
