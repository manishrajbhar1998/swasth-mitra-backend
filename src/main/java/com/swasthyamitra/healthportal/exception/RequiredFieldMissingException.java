package com.swasthyamitra.healthportal.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class RequiredFieldMissingException extends RuntimeException {
    private final List<String> errors;

    public RequiredFieldMissingException(List<String> errors) {
        super("Required fields are missing or invalid.");
        this.errors = errors;
    }

}
