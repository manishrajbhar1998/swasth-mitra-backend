package com.swasthyamitra.healthportal.service;

public interface EmailService {

    boolean sendEmail(String otp, String toEmail);
}
