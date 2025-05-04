package com.swasthyamitra.healthportal.mapper;


import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    EnquiryEntity convertEnquiryRequestToEnquiryEntity(EnquiryRequestVO enquiryRequest);

    EnquiryResponseVO convertEnquiryEntityToEnquiryResponse(EnquiryEntity savedEnquiry);

    UserInfoEntity convertUserRequestToUserInfoEntity(UserRequestVO userRequestVO);

    UserRequestVO convertUserInfoEntityToUserResponse(UserRequestVO userRequestVO);
}
