package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.dto.request.UserRequestVO;
import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import static com.swasthyamitra.healthportal.mapper.CommonMapper.mapper;
import com.swasthyamitra.healthportal.repository.UserInfoRepository;
import com.swasthyamitra.healthportal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserRequestVO addUser(UserRequestVO userRequestVO) {

        UserInfoEntity userInfoEntity = mapper.convertUserRequestToUserInfoEntity(userRequestVO);
        UserInfoEntity savedUserInfoEntity = userInfoRepository.save(userInfoEntity);
        return mapper.convertUserInfoEntityToUserResponse(userRequestVO);
    }
}
