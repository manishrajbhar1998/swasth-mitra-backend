package com.swasthyamitra.healthportal.repository;

import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanPurchaseRepository extends JpaRepository<PlanPurchaseEntity, UUID> {

    Optional<PlanPurchaseEntity> findByUserId(UUID userId);

    List<PlanPurchaseEntity> findByMemberId(String memberId);
}
