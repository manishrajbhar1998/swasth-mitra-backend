package com.swasthyamitra.healthportal.exception;


public class UnAuthorizedException extends RuntimeException {


    public UnAuthorizedException(String message) {
        super(message);
    }
}