package com.swasthyamitra.healthportal.scheduler;

import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;
import com.swasthyamitra.healthportal.repository.PlanPurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class PlanExpiryScheduler {

    private final PlanPurchaseRepository planRepo;

    public PlanExpiryScheduler(PlanPurchaseRepository planRepo) {
        this.planRepo = planRepo;
    }

    /**
     * Runs every day at midnight to expire plans older than 1 year.
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "${timezone:Asia/Kolkata}")
    public void expireOldPlans() {
        log.info("Plan expiry scheduler started");

        LocalDateTime cutoff = LocalDateTime.now().minusYears(1);
        List<PlanPurchaseEntity> plans = planRepo.findAllActiveWithExpiryBefore(cutoff);

        log.debug("Found {} plans to expire", plans.size());

        if (plans.isEmpty()) {
            log.info("No expired plans found. Scheduler finished.");
            return;
        }

        plans.forEach(plan -> {
            plan.setPlanExpiryDate(null);
            plan.setStatus("IN_ACTIVE");
            log.debug("Expiring plan: id={}, memberId={}", plan.getId(), plan.getMemberId());
        });

        planRepo.saveAll(plans);
        log.info("Expired {} plans and updated status", plans.size());
    }
}

