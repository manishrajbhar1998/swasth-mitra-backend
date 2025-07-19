package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.enums.RoleEnum;

import java.util.List;
import java.util.UUID;

public interface EnquiryService {
    EnquiryResponseVO saveEnquiry(EnquiryRequestVO enquiryRequest);

    List<EnquiryResponseVO> getAllEnquiries(RoleEnum role, String state, String district, String city ,Boolean delayedEnquiries);

    EnquiryResponseVO updateEnquiry(UUID enquiryId, EnquiryRequestVO enquiryRequest);

    EnquiryResponseVO getEnquiryById(UUID enquiryId);
}
