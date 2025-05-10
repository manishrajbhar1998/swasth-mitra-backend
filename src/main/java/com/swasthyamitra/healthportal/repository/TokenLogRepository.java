package com.swasthyamitra.healthportal.repository;

import com.swasthyamitra.healthportal.entity.TokenLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenLogRepository extends JpaRepository<TokenLogEntity, UUID> {
    Optional<TokenLogEntity> findFirstByUserIdAndIsValid(UUID id, Integer i);

    List<TokenLogEntity> findByUserIdAndIsValidAndCreatedAtGreaterThanEqual(UUID id, Integer i, LocalDateTime localDateTime);

    Optional<TokenLogEntity> findFirstByToken(String token);
}
