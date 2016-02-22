package com.nibodha.ip.services.json.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class JacksonObjectMapperFactoryBean extends AbstractFactoryBean<ObjectMapper> {

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    protected ObjectMapper createInstance() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper;
    }

}
