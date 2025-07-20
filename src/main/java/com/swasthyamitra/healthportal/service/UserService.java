package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.dto.response.UserResponseVO;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.enums.RoleEnum;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseVO addUser(UserRequestVO userRequestVO);

    List<UserResponseVO> getAllUsers(UserInfoEntity userInfoEntity, String role);

    UserResponseVO getUserById(UUID id);

    UserResponseVO updateUser(UUID  id, UserRequestVO userRequestVO);

    void deleteUser(UUID  id);

    void handleForgotPassword(String email);

    String resetPassword(String token, String password);
}
