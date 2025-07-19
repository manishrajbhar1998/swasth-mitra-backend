package com.swasthyamitra.healthportal.constants;

public class ErrorMessageConstants {

    private ErrorMessageConstants() {
        throw new AssertionError("Utility class ErrorMessageConstants cannot be instantiated");
    }


    public static final String USER_NOT_FOUND = "User does not exist or the account has been locked. Please contact to admin.";
    public static final String USER_NOT_CREDENTIALS_FOUND = "User Credentials is not found";


    public static final String RESOURCE_WITH_ID_NOT_FOUND = "%s with ID: %s";

    public static final String USER_EMAIL_EXISTS = "User Email is already exists";
    public static final String USER_NAME_EXISTS = "User Name is already exists";

    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token";

    public static final String INVALID_CURRENT_PASSWORD = "The current password provided is incorrect. Please try again.";
}
