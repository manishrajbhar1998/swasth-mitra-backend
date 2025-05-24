package com.swasthyamitra.healthportal.utils;

import com.swasthyamitra.healthportal.exception.RequiredFieldMissingException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class ValidationUtils {

    public static void Cc(Object obj) {
        List<String> missingFields = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                // Check NotBlank / NotNull validation
                if (field.isAnnotationPresent(NotBlank.class) || field.isAnnotationPresent(NotNull.class)) {
                    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                        // get message from annotation if present, else default message
                        String message = getValidationMessage(field, NotBlank.class, "Field " + field.getName() + " is required.");
                        if (value == null) {
                            message = getValidationMessage(field, NotNull.class, message);
                        }
                        missingFields.add(message);
                        continue; // skip pattern validation if missing required value
                    }
                }

                // Check Pattern validation (only if value is not null)
                if (value != null && value instanceof String && field.isAnnotationPresent(Pattern.class)) {
                    Pattern patternAnn = field.getAnnotation(Pattern.class);
                    String regex = patternAnn.regexp();
                    String strValue = (String) value;
                    try {
                        if (!strValue.matches(regex)) {
                            missingFields.add(patternAnn.message());
                        }
                    } catch (PatternSyntaxException e) {
                        missingFields.add("Invalid regex pattern on field: " + field.getName());
                    }
                }

            } catch (IllegalAccessException e) {
                missingFields.add("Error accessing field: " + field.getName());
            }
        }

        if (!missingFields.isEmpty()) {
            throw new RequiredFieldMissingException(missingFields);
        }
    }

    private static <T extends java.lang.annotation.Annotation> String getValidationMessage(Field field, Class<T> annotationClass, String defaultMsg) {
        try {
            T annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                if (annotation instanceof NotBlank) {
                    return ((NotBlank) annotation).message();
                } else if (annotation instanceof NotNull) {
                    return ((NotNull) annotation).message();
                }
            }
        } catch (Exception e) {
            // ignore and return default message
        }
        return defaultMsg;
    }

}
