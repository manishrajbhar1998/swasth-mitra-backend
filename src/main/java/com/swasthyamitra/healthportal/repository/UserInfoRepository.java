package com.swasthyamitra.healthportal.repository;


import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, UUID> {

    Optional<UserInfoEntity> findByIdAndIsDeletedFalse(UUID id);

    @Query("SELECT c FROM UserInfoEntity c ORDER BY c.createdAt DESC")
    Page<UserInfoEntity> findAllByIsDeletedFalse(Pageable pageable);

    Optional<UserInfoEntity> findByEmailAndIsDeletedFalse(String email);

    @Query("SELECT c FROM UserInfoEntity c  ORDER BY c.createdAt DESC")
    List<UserInfoEntity> findAllByIsDeletedFalse();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM UserInfoEntity c WHERE c.email = :email AND c.isDeleted = false")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM UserInfoEntity c WHERE c.phoneNumber = :phoneNumber AND c.isDeleted = false")
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.email = :email AND c.isDeleted = false")
    Optional<UserInfoEntity> findByEmail(String email);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum = :roleEnum AND LOWER(c.state) = LOWER(:state)")
    List<UserInfoEntity> findByRoleEnumAndState(@Param("roleEnum") RoleEnum roleEnum,
                                                @Param("state") String state);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum = :roleEnum AND LOWER(c.state) = LOWER(:state) AND LOWER(c.district) = LOWER(:district)")
    List<UserInfoEntity> findByRoleEnumAndStateAndDistrict(@Param("roleEnum") RoleEnum roleEnum,
                                                           @Param("state") String state,
                                                           @Param("district") String district);
    
    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum = :roleEnum")
    List<UserInfoEntity> findByRoleEnum(RoleEnum roleEnum);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum <> :roleEnum")
    List<UserInfoEntity> findAllByRoleEnumNot(@Param("roleEnum") RoleEnum roleEnum);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum = :roleEnum")
    List<UserInfoEntity> findAllByRoleEnum(@Param("roleEnum") RoleEnum roleEnum);

    @Query("SELECT c FROM UserInfoEntity c " +
            "WHERE c.roleEnum = :roleEnum " +
            "AND LOWER(c.state) = LOWER(:state) " +
            "AND LOWER(c.district) = LOWER(:district) " +
            "AND LOWER(c.city) = LOWER(:city)")
    List<UserInfoEntity> findByRoleEnumAndStateAndDistrictAndCity(@Param("roleEnum") RoleEnum roleEnum,
                                                                  @Param("state") String state,
                                                                  @Param("district") String district,
                                                                  @Param("city") String city);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum <> :roleEnum AND LOWER(c.state) = LOWER(:state)")
    List<UserInfoEntity> findByRoleEnumNotAndState(RoleEnum roleEnum, String state);

    @Query("SELECT c FROM UserInfoEntity c " +
            "WHERE c.roleEnum <> :roleEnum " +
            "AND LOWER(c.state) = LOWER(:state) " +
            "AND LOWER(c.district) = LOWER(:district) " +
            "AND LOWER(c.city) = LOWER(:city)")
    List<UserInfoEntity> findByRoleEnumNotAndStateAndDistrictAndCity(RoleEnum roleEnum, String state, String district, String city);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.roleEnum <> :roleEnum AND LOWER(c.state) = LOWER(:state) AND LOWER(c.district) = LOWER(:district)")
    List<UserInfoEntity> findByRoleEnumNotAndStateAndDistrict(RoleEnum roleEnum, String state, String district);

    List<UserInfoEntity> findByRoleEnumAndDistrictIgnoreCase(RoleEnum roleEnum, String district);
}
