package com.swasthyamitra.healthportal.convertor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConvertor implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ","; // use a consistent and safe delimiter

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        List<String> nonEmptyStrings = attribute.stream()
                .filter(str -> str != null && !str.isBlank())
                .collect(Collectors.toList());

        return String.join(DELIMITER, nonEmptyStrings);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.asList(dbData.split("\\Q" + DELIMITER + "\\E"));
    }
}
