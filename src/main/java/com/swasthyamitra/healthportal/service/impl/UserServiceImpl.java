package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.entity.PlanPurchaseEntity;
import com.swasthyamitra.healthportal.entity.TokenLogEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;

import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;

import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.exception.ExpiredTokenException;
import com.swasthyamitra.healthportal.exception.InvalidInputException;
import com.swasthyamitra.healthportal.exception.ResourceNotFoundException;
import com.swasthyamitra.healthportal.exception.UserExistsException;
import com.swasthyamitra.healthportal.repository.PlanPurchaseRepository;
import com.swasthyamitra.healthportal.repository.TokenLogRepository;
import com.swasthyamitra.healthportal.repository.UserInfoRepository;
import com.swasthyamitra.healthportal.service.EmailService;
import com.swasthyamitra.healthportal.service.UserService;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import com.swasthyamitra.healthportal.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserInfoRepository userInfoRepository;
    private final TokenLogRepository tokenLogRepository;
    private final EmailService emailService;
    private final PlanPurchaseRepository planPurchaseRepository;

    @Value("${app.otp.attempt}")
    Integer maxOtpAttempt;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository, TokenLogRepository tokenLogRepository, EmailService emailService, PlanPurchaseRepository planPurchaseRepository) {
        this.userInfoRepository = userInfoRepository;
        this.tokenLogRepository = tokenLogRepository;
        this.emailService = emailService;
        this.planPurchaseRepository = planPurchaseRepository;
    }

    @Override
    public UserResponseVO addUser(UserRequestVO userRequestVO) {

        if (userRequestVO.getPassword() == null || userRequestVO.getPassword().isEmpty()) {
            throw new InvalidInputException("Password is required");
        }

        ValidationUtils.Cc(userRequestVO);

        if (userInfoRepository.existsByEmail(userRequestVO.getEmail())) {
            throw new UserExistsException("Email already registered: " + userRequestVO.getEmail());
        }

       if (phoneNumber != null) {
        if (userInfoRepository.existsByPhoneNumber(phoneNumber)) {
        throw new UserExistsException("Phone Number already registered: " + phoneNumber);
       }
      }

        UserInfoEntity userInfoEntity = mapper.convertUserRequestToUserInfoEntity(userRequestVO);
        userInfoEntity.setEmail(userRequestVO.getEmail());
        userInfoEntity.setCreatedBy(userRequestVO.getCreatedBy());
        userInfoEntity.setPassword(userRequestVO.getPassword());
        userInfoEntity.setEncodedPassword(CommonUtils.hashPassword(userRequestVO.getPassword()));

        userInfoRepository.save(userInfoEntity);
        return mapper.convertUserInfoEntityToUserResponse(userInfoEntity);
    }

    @Override
    public List<UserResponseVO> getAllUsers(UserInfoEntity userInfoEntity, String filterRole) {
        List<UserInfoEntity> userInfoEntities;

        RoleEnum roleEnum = RoleEnum.valueOf(userInfoEntity.getRoleEnum().toString().toUpperCase());
        filterRole = (filterRole != null) ? filterRole.toUpperCase() : "";

        userInfoEntities = switch (roleEnum) {
            case SUPER_ADMIN -> switch (filterRole) {
                case "NOT_USER" -> userInfoRepository.findAllByRoleEnumNot(RoleEnum.USER);
                case "USER" -> userInfoRepository.findAllByRoleEnum(RoleEnum.USER);
                case "" -> userInfoRepository.findAllByIsDeletedFalse();
                default -> new ArrayList<>();
            };
            case STATE_ADMIN -> switch (filterRole) {
                case "NOT_USER" ->
                        userInfoRepository.findByRoleEnumNotAndState(RoleEnum.USER, userInfoEntity.getState());
                case "USER" -> userInfoRepository.findByRoleEnumAndState(RoleEnum.USER, userInfoEntity.getState());
                default -> new ArrayList<>();
            };
            case TEAM_LEADS, EMPLOYEE -> switch (filterRole) {
                case "NOT_USER" ->
                        userInfoRepository.findByRoleEnumNotAndStateAndDistrictAndCity(RoleEnum.USER, userInfoEntity.getState(),
                                userInfoEntity.getDistrict(), userInfoEntity.getCity());
                case "USER" ->
                        userInfoRepository.findByRoleEnumAndStateAndDistrictAndCity(RoleEnum.USER, userInfoEntity.getState(),
                                userInfoEntity.getDistrict(), userInfoEntity.getCity());
                default -> new ArrayList<>();
            };
            case DISTRICT_ADMIN, DISTRIBUTOR_ADMIN -> switch (filterRole) {
                case "NOT_USER" ->
                        userInfoRepository.findByRoleEnumNotAndStateAndDistrict(RoleEnum.USER, userInfoEntity.getState(),
                                userInfoEntity.getDistrict());
                case "USER" ->
                        userInfoRepository.findByRoleEnumAndStateAndDistrict(RoleEnum.USER, userInfoEntity.getState(),
                                userInfoEntity.getDistrict());
                default -> new ArrayList<>();
            };
            default -> new ArrayList<>();
        };


        return userInfoEntities.stream()
                .map(user -> {
                    UserResponseVO userResponseVO = mapper.convertUserInfoEntityToUserResponse(user);
                    userResponseVO.setStatus(user.isDeleted() ? "IN_ACTIVE" : "ACTIVE");

                    PlanPurchaseEntity planPurchaseEntity = planPurchaseRepository
                            .findByUserId(user.getId()) // or any logic
                            .orElse(null);

                    if (planPurchaseEntity != null) {
                        userResponseVO.setMemberId(planPurchaseEntity.getMemberId());
                        userResponseVO.setPlan(planPurchaseEntity.getPlan());
                        userResponseVO.setPaymentStatus(planPurchaseEntity.getPaymentStatus());
                        userResponseVO.setStatus(planPurchaseEntity.getStatus());
                        userResponseVO.setPlanExpiryDate(formatDate(planPurchaseEntity.getPlanExpiryDate()));
                    }

                    return userResponseVO;
                }).filter(userResponseVO -> !userResponseVO.getId().equals(userInfoEntity.getId()))
                .toList();

    }

    private String formatDate(Timestamp timestamp) {
        if (timestamp == null) return null;
        return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(timestamp);
    }

    @Override
    public UserResponseVO getUserById(UUID id) {
        UserInfoEntity user = userInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        PlanPurchaseEntity planPurchaseEntity = planPurchaseRepository
                .findByUserId(user.getId()) // or any logic
                .orElse(null);

        UserResponseVO userResponseVO = mapper.convertUserInfoEntityToUserResponse(user);
        userResponseVO.setStatus(user.isDeleted() ? "IN_ACTIVE" : "ACTIVE");

        if (planPurchaseEntity != null) {
            userResponseVO.setMemberId(planPurchaseEntity.getMemberId());
            userResponseVO.setPlan(planPurchaseEntity.getPlan());
            userResponseVO.setPaymentStatus(planPurchaseEntity.getPaymentStatus());
            userResponseVO.setStatus(planPurchaseEntity.getStatus());
            userResponseVO.setPlanExpiryDate(formatDate(planPurchaseEntity.getPlanExpiryDate()));
        }
        return userResponseVO;
    }

    @Override
    public UserResponseVO updateUser(UUID id, UserRequestVO userRequestVO) {

        ValidationUtils.Cc(userRequestVO);

        UserInfoEntity user = userInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        if (!user.getEmail().equalsIgnoreCase(userRequestVO.getEmail())) {
            if (userInfoRepository.existsByEmail(userRequestVO.getEmail())) {
                throw new UserExistsException("Email already registered: " + userRequestVO.getEmail());
            }
        }

     String existingPhone = user.getPhoneNumber();
     String newPhone = userRequestVO.getPhoneNumber();

// Check only if newPhone is not null and different from existingPhone
if (newPhone != null && !newPhone.equalsIgnoreCase(existingPhone)) {
    if (userInfoRepository.existsByPhoneNumber(newPhone)) {
        throw new UserExistsException("Phone Number already registered: " + newPhone);
    }
}

        UserInfoEntity userInfoEntity = mapper.convertUserRequestToUserInfoEntity(userRequestVO);
        userInfoEntity.setEmail(userRequestVO.getEmail());
        userInfoEntity.setPassword(user.getActualPassword());
        userInfoEntity.setEncodedPassword(user.getEncodedPassword());
        userInfoEntity.setCreatedBy(userInfoEntity.getCreatedBy());
        userInfoEntity.setUpdatedBy(userRequestVO.getUpdatedBy());
        userInfoEntity.setUpdatedAt(Timestamp.from(Instant.now()));
        userInfoEntity.setId(user.getId());

        boolean isActive = userRequestVO.getStatus().equalsIgnoreCase("ACTIVE");

        PlanPurchaseEntity planPurchaseEntity = planPurchaseRepository.findByUserId(userInfoEntity.getId())
                .orElse((null));
        if (planPurchaseEntity != null) {

        LocalDate expiryDate = planPurchaseEntity.getPlanExpiryDate().toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();

            if (!"SUCCESS".equalsIgnoreCase(planPurchaseEntity.getPaymentStatus()) ||
                    expiryDate.isBefore(today)) {
                throw new InvalidInputException("Your plan is either unpaid or has expired. Please renew your subscription.");
            }
            planPurchaseEntity.setStatus(isActive ? "ACTIVE" : "IN_ACTIVE");
            planPurchaseRepository.save(planPurchaseEntity);
        }

        userInfoEntity.setDeleted(!isActive);
        userInfoRepository.save(userInfoEntity);

        UserResponseVO userResponseVO = mapper.convertUserInfoEntityToUserResponse(userInfoEntity);
        userResponseVO.setStatus(user.isDeleted() ? "IN_ACTIVE" : "ACTIVE");

        if (planPurchaseEntity != null) {
            userResponseVO.setMemberId(planPurchaseEntity.getMemberId());
            userResponseVO.setPlan(planPurchaseEntity.getPlan());
            userResponseVO.setPaymentStatus(planPurchaseEntity.getPaymentStatus());
            userResponseVO.setStatus(planPurchaseEntity.getStatus());
            userResponseVO.setPlanExpiryDate(formatDate(planPurchaseEntity.getPlanExpiryDate()));
        }
        return userResponseVO;
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userInfoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete — user not found with ID: " + id);
        }

        userInfoRepository.deleteById(id);
    }

    @Override
    public void handleForgotPassword(String email) {
        log.info("Initiating forgot password process for email: {}", email);

        UserInfoEntity user = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Forgot password attempt failed — no user found with email: {}", email);
                    return new ResourceNotFoundException("User not found with Email: " + email);
                });

        log.debug("User found for forgot password: id={}, email={}", user.getId(), user.getEmail());

        String token = createForgotPasswordResetLog(user.getId(), user.getEmail());
        log.info("Reset token generated for user {}: {}", user.getEmail(), token);

        emailService.sendForgotPasswordMail(user.getEmail(), token);

        log.info("Forgot password email sent to: {}", user.getEmail());
    }

    @Override
    public String resetPassword(String token, String password) {

        TokenLogEntity tokenLog = verifyTokenForResetPassword(token);

        UserInfoEntity userInfoEntity = userInfoRepository.findById(tokenLog.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + tokenLog.getUserId()));

        userInfoEntity.setPassword(password);
        userInfoEntity.setEncodedPassword(CommonUtils.hashPassword(password));

        return userInfoRepository.save(userInfoEntity).getRoleEnum().toString();
    }


    public TokenLogEntity verifyTokenForResetPassword(String token) {
        Optional<TokenLogEntity> tokenLogO = tokenLogRepository.findFirstByToken(token);
        if (!tokenLogO.isPresent()) {
            throw new ExpiredTokenException("Link has been expired or invalid");
        }
        TokenLogEntity tokenLog = tokenLogO.get();

        this.setTokenLogAttempted(tokenLog);
        if (tokenLog.getIsValid() != 1 || tokenLog.getExpiredAt().isBefore(OffsetDateTime.now())) {
            throw new ExpiredTokenException("Link has been expired or invalid");

        }

        if (tokenLog.getAttempt() > maxOtpAttempt) {
            throw new ExpiredTokenException("You have exceeded login attempts");
        }
        return tokenLog;
    }

    public void setTokenLogAttempted(TokenLogEntity tokenLog) {
        tokenLog.setAttempt(tokenLog.getAttempt() + 1);
        tokenLogRepository.save(tokenLog);
    }

    private String createForgotPasswordResetLog(UUID id, String email) {

        Optional<TokenLogEntity> tokenLog = tokenLogRepository.findFirstByUserIdAndIsValid(id, 1);

        if (tokenLog.isPresent()) {
            TokenLogEntity tokenLog1 = tokenLog.get();
            if (OffsetDateTime.now().isBefore(tokenLog1.getExpiredAt())) {
                throw new ExpiredTokenException("Password recovery mail already sent. Please check your spam or wait for 10  min for new reset request");
            }
            tokenLog1.setIsValid(0);
            tokenLogRepository.save(tokenLog1);
        }

        List<TokenLogEntity> tokenLogs = tokenLogRepository
                .findByUserIdAndIsValidAndCreatedAtGreaterThanEqual(id, 1, LocalDateTime.now().minusMinutes(10));
        if (tokenLogs.size() > 1) {
            throw new ExpiredTokenException("Password recovery mail already sent. Please check your spam or wait for 10  min for new reset request");
        }

        OffsetDateTime expirationDate = OffsetDateTime.now().plusMinutes(10L);

        TokenLogEntity tl = new TokenLogEntity();
        tl.setUserId(id);
        tl.setEmail(email);
        tl.setToken(CommonUtils.generateToken(id));
        tl.setExpiredAt(expirationDate);
        tl.setAttempt(0);
        tl.setIsValid(1);
        tl = tokenLogRepository.save(tl);

        return tl.getToken();
    }
}
