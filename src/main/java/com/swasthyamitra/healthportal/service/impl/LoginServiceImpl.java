package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.confg.JwtService;
import com.swasthyamitra.healthportal.dto.request.LoginRequestVO;
import com.swasthyamitra.healthportal.dto.request.ValidateTokenRequestVO;
import com.swasthyamitra.healthportal.dto.response.JwtResponseVO;
import com.swasthyamitra.healthportal.dto.response.ValidateTokenResponseVO;
import com.swasthyamitra.healthportal.entity.TokenEntity;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.enums.TokenType;
import com.swasthyamitra.healthportal.repository.TokenRepository;
import com.swasthyamitra.healthportal.repository.UserInfoRepository;
import com.swasthyamitra.healthportal.service.LoginService;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import com.swasthyamitra.healthportal.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swasthyamitra.healthportal.constants.ErrorMessageConstants;
import com.swasthyamitra.healthportal.exception.*;

import java.util.concurrent.ExecutionException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserInfoRepository userInfoRepository;


    @Override
    public JwtResponseVO authenticateUser(LoginRequestVO loginRequest) throws ExecutionException, InterruptedException {

        ValidationUtils.Cc(loginRequest);

        UserInfoEntity userAuth = userInfoRepository.findByEmailAndIsDeletedFalse(loginRequest.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessageConstants.USER_NOT_FOUND));

        boolean validPassword = CommonUtils.verifyPassword(loginRequest.getPassword(), userAuth.getEncodedPassword());

        if (!validPassword) {
            throw new InvalidPasswordException("Invalid password");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new ResourceNotFoundException(String.format(
                    "User login failed: %s", loginRequest.getUserName()));
        }

        var jwtToken = jwtService.generateToken(userAuth);
        var refreshToken = jwtService.generateRefreshToken(userAuth);

        this.revokeAllUserTokens(userAuth);

        saveUserToken(userAuth, jwtToken);

        RoleEnum roleEnum = userAuth.getRoleEnum();

        return new JwtResponseVO(jwtToken, refreshToken, userAuth.getUsername(), userAuth.getId(), roleEnum);
    }

    @Override
    public ValidateTokenResponseVO validateToken(ValidateTokenRequestVO request) {

        try {
            var token = tokenRepository.findByJwtToken(request.getToken());
            if (token.isEmpty()) {
                throw new InvalidPasswordException("Invalid token");
            }
            boolean isTokenValid = jwtService.isTokenValid(request.getToken(), token.get().getUserInfo());
            if (isTokenValid) {
                return ValidateTokenResponseVO.builder()
                        .status(200)
                        .data(true)
                        .build();
            } else {
                return ValidateTokenResponseVO.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .data(false)
                        .build();
            }
        } catch (Exception e) {
            return ValidateTokenResponseVO.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(false)
                    .build();
        }
    }

    private void saveUserToken(UserInfoEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .userInfo(user)
                .jwtToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserInfoEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
