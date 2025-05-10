package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.constants.ApiResponseMessages;
import com.swasthyamitra.healthportal.dto.request.ForgotPasswordRequest;
import com.swasthyamitra.healthportal.dto.request.LoginRequestVO;
import com.swasthyamitra.healthportal.dto.request.ResetPasswordRequest;
import com.swasthyamitra.healthportal.dto.request.ValidateTokenRequestVO;
import com.swasthyamitra.healthportal.dto.response.ApiResponse;
import com.swasthyamitra.healthportal.dto.response.JwtResponseVO;
import com.swasthyamitra.healthportal.dto.response.ValidateTokenResponseVO;
import com.swasthyamitra.healthportal.service.LoginService;
import com.swasthyamitra.healthportal.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthAPI {

    private final LoginService loginService;
    private final UserService userService;

    @Autowired
    public AuthAPI(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JwtResponseVO>> authenticateUser(@Valid @RequestBody LoginRequestVO loginRequest) throws ExecutionException, InterruptedException {
        log.info("Received login request for user: {}", loginRequest.getUserName());
        JwtResponseVO jwtResponseVO = loginService.authenticateUser(loginRequest);
        log.info("User authenticated successfully: {}", jwtResponseVO.getUsername());
        ApiResponse<JwtResponseVO> response = ApiResponse.<JwtResponseVO>builder()
                .data(jwtResponseVO)
                .message(ApiResponseMessages.USER_LOGGED_IN_SUCCESSFULLY)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ValidateTokenResponseVO>> validateToken(@RequestBody ValidateTokenRequestVO request) {
        log.info("Received token validation request: {}", request);
        ValidateTokenResponseVO responseDTO = loginService.validateToken(request);
        log.info("Token validation successful for token: {}", request.getToken());
        ApiResponse<ValidateTokenResponseVO> response = ApiResponse.<ValidateTokenResponseVO>builder()
                .data(responseDTO)
                .message(ApiResponseMessages.TOKEN_VALIDATED_SUCCESSFULLY)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        log.info("Reset password request received with token: {}", resetPasswordRequest.getToken());

        userService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Password has been reset successfully.")
                        .build()
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        log.info("Forgot password request received for email: {}", request.getEmail());

        userService.handleForgotPassword(request.getEmail());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Your reset password link has been sent to your registered email address")
                        .build()
        );
    }

}
