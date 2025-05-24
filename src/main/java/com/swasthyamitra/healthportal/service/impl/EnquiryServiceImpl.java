package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;
import com.swasthyamitra.healthportal.repository.EnquiryRepository;
import com.swasthyamitra.healthportal.service.EnquiryService;
import com.swasthyamitra.healthportal.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;

    @Autowired
    public EnquiryServiceImpl(EnquiryRepository enquiryRepository){
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    public EnquiryResponseVO saveEnquiry(EnquiryRequestVO enquiryRequest) {
        log.info("Saving new enquiry for user: {}", enquiryRequest.getUserName());
        ValidationUtils.Cc(enquiryRequest);

        EnquiryEntity enquiryEntity = mapper.convertEnquiryRequestToEnquiryEntity(enquiryRequest);
        enquiryEntity.setCreatedBy(enquiryRequest.getCreatedBy());
        EnquiryEntity savedEnquiry = enquiryRepository.save(enquiryEntity);

        log.info("Enquiry saved successfully with ID: {}", savedEnquiry.getId());
        return mapper.convertEnquiryEntityToEnquiryResponse(savedEnquiry);
    }

    @Override
    public List<EnquiryResponseVO> getAllEnquiries() {
        log.info("Fetching all enquiries from the database...");

        List<EnquiryEntity> enquiryEntities = enquiryRepository.findAll();

        log.info("Total enquiries fetched: {}", enquiryEntities.size());
        return enquiryEntities.stream()
                .map(mapper::convertEnquiryEntityToEnquiryResponse)
                .toList();
    }
}
