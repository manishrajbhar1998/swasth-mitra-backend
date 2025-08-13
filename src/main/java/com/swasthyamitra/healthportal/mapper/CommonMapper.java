package com.swasthyamitra.healthportal.mapper;


import com.swasthyamitra.healthportal.dto.request.EnquiryRequestVO;
import com.swasthyamitra.healthportal.dto.request.PlanPurchaseRequestDTO;
import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.EnquiryResponseVO;
import com.swasthyamitra.healthportal.dto.response.PlanPurchaseResponseDTO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import com.swasthyamitra.healthportal.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.Date;

@Mapper(componentModel = "spring", imports = CommonUtils.class)
public interface CommonMapper {

    CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    EnquiryEntity convertEnquiryRequestToEnquiryEntity(EnquiryRequestVO enquiryRequest);

    @Mapping(source = "createdByUUID", target = "followUpUUID")
    @Mapping(source = "userInfo", target = "followUpBy", qualifiedByName = "mapFullName")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    EnquiryResponseVO convertEnquiryEntityToEnquiryResponse(EnquiryEntity savedEnquiry);

    @Mapping(target = "roleEnum", expression = "java(CommonUtils.toValidRole(userRequestVO.getRole()))")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "stringToDateSafe")
    UserInfoEntity convertUserRequestToUserInfoEntity(UserRequestVO userRequestVO);

    @Mapping(target = "role", source = "roleEnum")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "dateToStringSafe")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
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

    @Named("mapFullName")
    static String mapFullName(UserInfoEntity userInfo) {
        return userInfo == null ? "" : userInfo.getFirstName() + " " + userInfo.getLastName();
    }


    @Mapping(target = "profilePic", ignore = true)
    PlanPurchaseEntity convertPlanPurchaseRequesToPlanPurchaseEntity(PlanPurchaseRequestDTO requestDTO);

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "planExpiryDate", expression = "java(formatDate(planPurchaseEntity.getPlanExpiryDate()))")
    @Mapping(target = "profilePic", expression = "java(planPurchaseEntity.getProfilePicBase64())")
    PlanPurchaseResponseDTO convertPlanPurchaseEntityToPlanPurchaseResponseDTO(PlanPurchaseEntity planPurchaseEntity);

    default String formatDate(Timestamp timestamp) {
        if (timestamp == null) return null;
        return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(timestamp);
    }
}
