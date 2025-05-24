package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.dto.request.LoginRequestVO;
import com.swasthyamitra.healthportal.dto.request.ValidateTokenRequestVO;
import com.swasthyamitra.healthportal.dto.response.JwtResponseVO;
import com.swasthyamitra.healthportal.dto.response.ValidateTokenResponseVO;

import java.util.concurrent.ExecutionException;

public interface LoginService {

    JwtResponseVO authenticateUser(LoginRequestVO loginRequest) throws ExecutionException, InterruptedException;

    ValidateTokenResponseVO validateToken(ValidateTokenRequestVO request);

}
