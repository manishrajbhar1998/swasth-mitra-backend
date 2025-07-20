package com.swasthyamitra.healthportal.repository;

import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface EnquiryRepository extends JpaRepository<EnquiryEntity, UUID> {
    List<EnquiryEntity> findByStateIgnoreCase(String state);

    List<EnquiryEntity> findByStateIgnoreCaseAndDistrictIgnoreCase(String state, String district);

    List<EnquiryEntity> findByStateIgnoreCaseAndDistrictIgnoreCaseAndCityIgnoreCase(String state, String district, String city);

    List<EnquiryEntity> findByStatusIsNull();

    @Query("SELECT e FROM EnquiryEntity e WHERE e.status IS NULL AND e.createdAt >= :oneHourAgo")
    List<EnquiryEntity> findRecentNullStatusEnquiries(Timestamp oneHourAgo);
}
