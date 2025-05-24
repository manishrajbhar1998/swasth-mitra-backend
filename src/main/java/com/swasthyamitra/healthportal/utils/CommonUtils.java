package com.swasthyamitra.healthportal.utils;

import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.exception.InvalidInputException;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.UUID;

public class CommonUtils {

    private static final SecureRandom random = new SecureRandom();

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String[] PREFIXES = {"golden", "silver", "bronze", "platinum", "diamond"};

    private CommonUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String generateDummyPassword() {
        return PREFIXES[random.nextInt(PREFIXES.length)] + getRandomNumber();
    }

    private static String getRandomNumber() {
        return String.valueOf(1000 + random.nextInt(9000));
    }

    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static RoleEnum toValidRole(String role) {
        if (StringUtils.isBlank(role)) {
            throw new InvalidInputException("Role must not be empty.");
        }

        try {
            return RoleEnum.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid role: " + role);
        }
    }

    public static String generateToken(UUID id) {

        long token1 = random.nextLong();
        long token2 = random.nextLong();
        token1=(token1==Long.MIN_VALUE)?Long.MAX_VALUE:Math.abs(token1);
        token2=(token2==Long.MIN_VALUE)?Long.MAX_VALUE:Math.abs(token2);
        String randomString = Long.toString(token1, 30) + Long.toString(token2, 30) + id;
        randomString = randomString.replace("/", "").replace("\\", "");
        return randomString;
    }
}
