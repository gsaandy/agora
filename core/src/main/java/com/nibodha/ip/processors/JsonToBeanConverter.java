package com.nibodha.ip.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class JsonToBeanConverter implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonToBeanConverter.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonToBeanConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        final String body = exchange.getIn().getBody(String.class);
        if (body != null) {
            final Object result = transform(exchange, body);
            exchange.getIn().setBody(result);
        }
    }

    private Object transform(final Exchange exchange, final String body) throws ClassNotFoundException, IOException {
        final Object result;
        final Class<?> clazz;
        final String type = exchange.getIn().getHeader("ConvertJsonToBeanType", String.class);
        final boolean useCollection = exchange.getIn().getHeader("UseJsonCollection", boolean.class);
        if (type != null) {
            clazz = exchange.getContext().getClassResolver().resolveMandatoryClass(type);
        } else {
            logInfo("No Json Unmarshal Type specified ::: setting default to HashMap");
            clazz = HashMap.class;
        }

        if (useCollection) {
            final CollectionType collType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            result = this.objectMapper.readValue(body, collType);
        } else {
            result = this.objectMapper.readValue(body, clazz);
        }
        return result;
    }

    private void logInfo(final String message) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(message);
        }
    }
}
