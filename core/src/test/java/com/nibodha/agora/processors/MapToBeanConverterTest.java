package com.nibodha.agora.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapToBeanConverterTest extends CamelTestSupport {

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    protected Exchange exchange;

    @Override
    protected void doPostSetup() throws Exception {
        exchange = new DefaultExchange(this.context());
    }

    @Test
    public void dontConvertNull() {
        template.send(exchange);
        assertNull(exchange.getIn().getBody());
    }

    @Test
    public void convertMap() {
        final Map<String, Object> message = new HashMap<>();
        final String code = "key";
        final String value = "Value";
        message.put("code", code);
        message.put("value", value);
        exchange.getIn().setBody(message);
        exchange.getIn().setHeader("ConvertMapToBeanType", "com.nibodha.agora.processors.TestPojo");
        template.send(exchange);
        final TestPojo testPojo = exchange.getIn().getBody(TestPojo.class);
        assertNotNull(testPojo);
        assertEquals(code, testPojo.getCode());
        assertEquals(value, testPojo.getValue());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").process(new MapToBeanConverter(new ObjectMapper())).to("mock:result");
            }
        };
    }

}