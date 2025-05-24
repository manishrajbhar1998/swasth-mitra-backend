package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.enums.RoleEnum;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseVO addUser(UserRequestVO userRequestVO);

    List<UserResponseVO> getAllUsers(RoleEnum roleEnum, String state, String district);

    UserResponseVO getUserById(UUID id);

    UserResponseVO updateUser(UUID  id, UserRequestVO userRequestVO);

    void deleteUser(UUID  id);

    void handleForgotPassword(String email);

    void resetPassword(String token, String password);
}
