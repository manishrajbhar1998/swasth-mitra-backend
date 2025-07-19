package com.swasthyamitra.healthportal.repository;

import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnquiryRepository extends JpaRepository<EnquiryEntity, UUID> {
    List<EnquiryEntity> findByStateIgnoreCase(String state);

    List<EnquiryEntity> findByStateIgnoreCaseAndDistrictIgnoreCase(String state, String district);

    List<EnquiryEntity> findByStateIgnoreCaseAndDistrictIgnoreCaseAndCityIgnoreCase(String state, String district, String city);

    List<EnquiryEntity> findByStatusIsNull();
}
