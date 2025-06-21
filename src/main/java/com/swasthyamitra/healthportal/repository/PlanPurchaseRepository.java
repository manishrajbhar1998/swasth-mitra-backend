package com.swasthyamitra.healthportal.repository;

import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanPurchaseRepository extends JpaRepository<PlanPurchaseEntity, UUID> {

    Optional<PlanPurchaseEntity> findByUserId(UUID userId);

    List<PlanPurchaseEntity> findByMemberId(String memberId);

    @Query("SELECT p FROM PlanPurchaseEntity p "
            + "WHERE p.planExpiryDate IS NOT NULL "
            + "AND p.planExpiryDate < :cutoff "
            + "AND p.status = 'ACTIVE'")
    List<PlanPurchaseEntity> findAllActiveWithExpiryBefore(@Param("cutoff") LocalDateTime cutoff);
}
