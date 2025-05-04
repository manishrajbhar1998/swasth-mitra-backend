package com.swasthyamitra.healthportal.convertor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class StringListConvertor implements AttributeConverter<List<String>, String> {

    @Value("${list.delimiter}")
    private String delimiter;

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        List<String> nonEmptyStrings = attribute.stream()
                .filter(str -> str != null && !str.isEmpty())
                .toList();
        return String.join(delimiter, nonEmptyStrings);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(dbData.split("\\Q" + delimiter + "\\E"));
    }
}
