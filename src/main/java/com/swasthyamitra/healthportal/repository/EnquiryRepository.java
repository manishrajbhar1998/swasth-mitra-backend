package com.swasthyamitra.healthportal.repository;

import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnquiryRepository extends JpaRepository<EnquiryEntity, UUID> {
}
