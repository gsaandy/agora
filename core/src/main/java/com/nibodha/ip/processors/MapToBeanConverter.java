package com.nibodha.ip.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MapToBeanConverter implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapToBeanConverter.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public MapToBeanConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        final String className = exchange.getIn().getHeader("MapToBeanType", String.class);
        final Map payload = exchange.getIn().getBody(Map.class);

        if (className != null && payload != null) {
            final Class clazz = exchange.getContext().getClassResolver().resolveMandatoryClass(className);
            final Object result = objectMapper.convertValue(payload, clazz);
            exchange.getIn().setBody(result);
        } else {
            LOGGER.info("No result type or payload found, skipping transformation");
        }
    }
}
