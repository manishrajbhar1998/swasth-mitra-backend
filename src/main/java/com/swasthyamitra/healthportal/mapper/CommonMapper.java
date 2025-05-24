package com.swasthyamitra.healthportal.mapper;


import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import com.swasthyamitra.healthportal.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper(componentModel = "spring",imports = CommonUtils.class)
public interface CommonMapper {

    CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    EnquiryEntity convertEnquiryRequestToEnquiryEntity(EnquiryRequestVO enquiryRequest);

    EnquiryResponseVO convertEnquiryEntityToEnquiryResponse(EnquiryEntity savedEnquiry);

    @Mapping(target = "roleEnum", expression = "java(CommonUtils.toValidRole(userRequestVO.getRole()))")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "stringToDateSafe")
    UserInfoEntity convertUserRequestToUserInfoEntity(UserRequestVO userRequestVO);

    @Mapping(target = "role", source = "roleEnum")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "dateToStringSafe")
    UserResponseVO convertUserInfoEntityToUserResponse(UserInfoEntity userInfoEntity);

    @Named("stringToDateSafe")
    public static Date stringToDateSafe(String dateStr) {
        try {
            return DateUtils.stringToDate(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format for dateOfBirth. Expected dd-MM-yyyy");
        }
    }

    @Named("dateToStringSafe")
    static String dateToStringSafe(Date date) {
        return DateUtils.dateToString(date);
    }
}
