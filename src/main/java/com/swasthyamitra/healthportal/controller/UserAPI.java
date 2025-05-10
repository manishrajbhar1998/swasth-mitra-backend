package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.dto.request.ForgotPasswordRequest;
import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.ApiResponse;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/registration")
@Slf4j
public class UserAPI {

    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponseVO>> submitRegistration(
            @Valid @RequestBody UserRequestVO userRequestVO) {

        log.info("Received registration submission from user: {}", userRequestVO.getEmail());

        UserResponseVO registrationResponse = userService.addUser(userRequestVO);

        ApiResponse<UserResponseVO> response = ApiResponse.<UserResponseVO>builder()
                .data(registrationResponse)
                .message("Registration submitted successfully.")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // READ ALL
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ApiResponse<List<UserResponseVO>>> getAllUsers(@RequestParam(required = false)String role) {
        log.info("Fetching all users");

        List<UserResponseVO> users = userService.getAllUsers(role);

        ApiResponse<List<UserResponseVO>> response = ApiResponse.<List<UserResponseVO>>builder()
                .data(users)
                .message("Users retrieved successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    // READ ONE
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponseVO>> getUserById(@PathVariable UUID id) {
        log.info("Fetching user with id: {}", id);

        UserResponseVO user = userService.getUserById(id);

        ApiResponse<UserResponseVO> response = ApiResponse.<UserResponseVO>builder()
                .data(user)
                .message("User retrieved successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponseVO>> updateUser(
            @PathVariable UUID  id,
            @Valid @RequestBody UserRequestVO userRequestVO) {

        log.info("Updating user with id: {}", id);

        UserResponseVO updatedUser = userService.updateUser(id, userRequestVO);

        ApiResponse<UserResponseVO> response = ApiResponse.<UserResponseVO>builder()
                .data(updatedUser)
                .message("User updated successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        log.info("Deleting user with id: {}", id);

        userService.deleteUser(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("User deleted successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

}
