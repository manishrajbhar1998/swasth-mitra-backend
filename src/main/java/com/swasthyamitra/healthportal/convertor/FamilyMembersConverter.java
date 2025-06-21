package com.swasthyamitra.healthportal.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swasthyamitra.healthportal.dto.request.FamilyMembersDTO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class FamilyMembersConverter implements AttributeConverter<FamilyMembersDTO, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(FamilyMembersDTO attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize FamilyMembersDTO to JSON", e);
        }
    }

    @Override
    public FamilyMembersDTO convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return objectMapper.readValue(dbData, FamilyMembersDTO.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not deserialize JSON to FamilyMembersDTO", e);
        }
    }
}
