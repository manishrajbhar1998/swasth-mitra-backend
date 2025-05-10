package com.swasthyamitra.healthportal.service;

import com.swasthyamitra.healthportal.entity.UserInfoEntity;

public interface EmailService {

    boolean sendEmail(String otp, String toEmail);

    void sendForgotPasswordMail(String email, String token);
}
