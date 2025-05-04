package com.swasthyamitra.healthportal.repository;


import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, UUID> {

    Optional<UserInfoEntity> findByIdAndIsDeletedFalse(UUID id);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<UserInfoEntity> findAllByIsDeletedFalse(Pageable pageable);

    Optional<UserInfoEntity> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    @Query("SELECT c FROM UserInfoEntity c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    List<UserInfoEntity> findAllByIsDeletedFalse();


}
