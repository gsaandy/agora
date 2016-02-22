package com.nibodha.ip.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectToJsonConverter implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectToJsonConverter.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public ObjectToJsonConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Object body = exchange.getIn().getBody();
        if (body != null) {
            LOGGER.info("Transforming body {} to json", body);
            final String jsonBody = objectMapper.writeValueAsString(body);
            exchange.getIn().setBody(jsonBody);
        }
    }

}

