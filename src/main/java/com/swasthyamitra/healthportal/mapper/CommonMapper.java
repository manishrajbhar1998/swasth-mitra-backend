package com.swasthyamitra.healthportal.mapper;


import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",imports = CommonUtils.class)
public interface CommonMapper {

    CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    EnquiryEntity convertEnquiryRequestToEnquiryEntity(EnquiryRequestVO enquiryRequest);

    EnquiryResponseVO convertEnquiryEntityToEnquiryResponse(EnquiryEntity savedEnquiry);

    @Mapping(target = "roleEnum", expression = "java(CommonUtils.toValidRole(userRequestVO.getRole()))")
    @Mapping(target = "email", ignore = true)
    UserInfoEntity convertUserRequestToUserInfoEntity(UserRequestVO userRequestVO);

    @Mapping(target = "role", source = "roleEnum")
    UserResponseVO convertUserInfoEntityToUserResponse(UserInfoEntity userInfoEntity);
}
