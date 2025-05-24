package com.swasthyamitra.healthportal.exception;

import java.util.List;

public class RequiredFieldMissingException extends RuntimeException {
    private final List<String> errors;

    public RequiredFieldMissingException(List<String> errors) {
        super("Required fields are missing or invalid.");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
