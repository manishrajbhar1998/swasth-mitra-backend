package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;

import java.util.List;

public interface EnquiryService {
    EnquiryResponseVO saveEnquiry(EnquiryRequestVO enquiryRequest);

    List<EnquiryResponseVO> getAllEnquiries();
}
