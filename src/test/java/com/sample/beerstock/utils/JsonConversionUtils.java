package com.sample.beerstock.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConversionUtils {
    public static String asJsonString(Object cervejaDTO) {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objMapper.registerModules(new JavaTimeModule());
            return objMapper.writeValueAsString(cervejaDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
