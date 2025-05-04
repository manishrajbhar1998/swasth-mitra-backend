package com.swasthyamitra.healthportal.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.StringUtils;

import java.util.*;

@Converter
@Slf4j
public class MapStringConvertor implements AttributeConverter<Map<String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        ObjectMapper objectMapper = new ObjectMapper();
        String value = "{}";
        try {
            value = objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting map to JSON", e);
        }
        return value;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        Map<String, Object> dataMap = new HashMap<>();

        try {
            if (StringUtils.hasText(dbData)) {
                dataMap = new ObjectMapper().readValue(dbData, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to map", e);
        }

        return dataMap;
    }
}
