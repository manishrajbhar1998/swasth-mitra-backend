package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.entity.TokenLogEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;

import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;

import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.exception.ExpiredTokenException;
import com.swasthyamitra.healthportal.exception.ResourceNotFoundException;
import com.swasthyamitra.healthportal.exception.UserExistsException;
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

    @Value("${app.otp.attempt}")
    Integer maxOtpAttempt;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository, TokenLogRepository tokenLogRepository, EmailService emailService) {
        this.userInfoRepository = userInfoRepository;
        this.tokenLogRepository = tokenLogRepository;
        this.emailService = emailService;
    }

    @Override
    public UserResponseVO addUser(UserRequestVO userRequestVO) {

        ValidationUtils.Cc(userRequestVO);

        if (userInfoRepository.existsByEmail(userRequestVO.getEmail())) {
            throw new UserExistsException("Email already registered: " + userRequestVO.getEmail());
        }

        if (userInfoRepository.existsByPhoneNumber(userRequestVO.getPhoneNumber())) {
            throw new UserExistsException("Phone Number already registered: " + userRequestVO.getPhoneNumber());
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
    public List<UserResponseVO> getAllUsers(RoleEnum authorizeRole, String state, String district, String filterRole) {
        List<UserInfoEntity> userInfoEntities;

        RoleEnum roleEnum = CommonUtils.toValidRole("USER");

        if ("SUPER_ADMIN".equalsIgnoreCase(authorizeRole.toString())) {
            // No role filter — fetch all
            RoleEnum superAdminRole = CommonUtils.toValidRole(filterRole);
            userInfoEntities = userInfoRepository.findByRoleEnum(superAdminRole);
        } else if ("STATE_ADMIN".equalsIgnoreCase(authorizeRole.toString())) {
            userInfoEntities = userInfoRepository.findByRoleEnumAndState(roleEnum, state);
        } else if ("USER".equalsIgnoreCase(authorizeRole.toString())) {
            userInfoEntities = new ArrayList<>();
        }else {
            userInfoEntities = userInfoRepository.findByRoleEnumAndStateAndDistrict(roleEnum, state, district);
        }

        return userInfoEntities.stream()
                .map(mapper::convertUserInfoEntityToUserResponse)
                .toList();
    }

    @Override
    public UserResponseVO getUserById(UUID id) {
        UserInfoEntity user = userInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        return mapper.convertUserInfoEntityToUserResponse(user);
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

        if (!user.getPhoneNumber().equalsIgnoreCase(userRequestVO.getPhoneNumber())) {
            if (userInfoRepository.existsByPhoneNumber(userRequestVO.getPhoneNumber())) {
                throw new UserExistsException("Phone Number already registered: " + userRequestVO.getPhoneNumber());
            }
        }

        UserInfoEntity userInfoEntity = mapper.convertUserRequestToUserInfoEntity(userRequestVO);
        userInfoEntity.setEmail(userRequestVO.getEmail());
        userInfoEntity.setUpdatedBy(userRequestVO.getUpdatedBy());
        userInfoEntity.setId(user.getId());

        userInfoRepository.save(userInfoEntity);

        return mapper.convertUserInfoEntityToUserResponse(userInfoEntity);
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
    public void resetPassword(String token, String password) {

        TokenLogEntity tokenLog = verifyTokenForResetPassword(token);

        UserInfoEntity userInfoEntity = userInfoRepository.findById(tokenLog.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + tokenLog.getUserId()));

        userInfoEntity.setPassword(password);
        userInfoEntity.setEncodedPassword(CommonUtils.hashPassword(password));

        userInfoRepository.save(userInfoEntity);
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
