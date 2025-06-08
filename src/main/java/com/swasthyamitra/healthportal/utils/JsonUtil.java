package com.swasthyamitra.healthportal.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.service.spi.ServiceException;

public class JsonUtil {

    public static <T> T toTypeReferenceObject(String str, TypeReference<T> typeRef) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(str, typeRef);
        } catch (Exception e) {
            throw new ServiceException(e.toString());
        }
    }
}
