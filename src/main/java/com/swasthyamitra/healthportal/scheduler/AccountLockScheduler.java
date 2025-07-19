package com.swasthyamitra.healthportal.scheduler;

import com.swasthyamitra.healthportal.entity.EnquiryEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.repository.EnquiryRepository;
import com.swasthyamitra.healthportal.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AccountLockScheduler {

    private final EnquiryRepository enquiryRepository;
    private final UserInfoRepository userInfoRepository;

    public AccountLockScheduler(EnquiryRepository enquiryRepository, UserInfoRepository userInfoRepository) {
        this.enquiryRepository = enquiryRepository;
        this.userInfoRepository = userInfoRepository;
    }

    // Scheduler: runs every 5 minutes
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void lockTeamLeadsForDelayedEnquiries() {
        log.info("Running district-based Account Lock Scheduler...");

        // Step 1: Get all enquiries where status is null
        List<EnquiryEntity> pendingEnquiries = enquiryRepository.findByStatusIsNull();
        log.info("Found {} enquiries with null status", pendingEnquiries.size());

        // Step 2: Group enquiries by district (case-insensitive)
        Map<String, Long> districtCounts = pendingEnquiries.stream()
                .filter(e -> e.getDistrict() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getDistrict().toLowerCase(),
                        Collectors.counting()
                ));

        // Step 3: Check districts with >= 5 delayed enquiries
        districtCounts.entrySet().stream()
                .filter(entry -> entry.getValue() >= 5)
                .forEach(entry -> {
                    String district = entry.getKey();
                    log.info("District '{}' has {} delayed enquiries — locking team leads...", district, entry.getValue());

                    // Step 4: Get all TEAM_LEADS from that district
                    List<UserInfoEntity> teamLeads = userInfoRepository.findByRoleEnumAndDistrictIgnoreCase(RoleEnum.TEAM_LEADS, district);

                    // Step 5: Mark them as deleted
                    for (UserInfoEntity user : teamLeads) {
                        user.setDeleted(true);
                    }

                    userInfoRepository.saveAll(teamLeads);
                    log.info("Locked {} team leads in district '{}'", teamLeads.size(), district);
                });

        log.info("District-based Account Lock Scheduler completed.");
    }
}
