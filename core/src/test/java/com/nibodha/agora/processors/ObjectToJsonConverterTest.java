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

public class ObjectToJsonConverterTest extends CamelTestSupport {

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
    public void convertObjectToJson() {
        final TestPojo testPojo = new TestPojo();
        testPojo.setCode("Code 1");
        testPojo.setValue("Value 1");
        exchange.getIn().setBody(testPojo);
        template.send(exchange);
        final String testPojoJson = exchange.getIn().getBody(String.class);
        assertNotNull(testPojoJson);
        assertEquals("{\"code\":\"Code 1\",\"value\":\"Value 1\"}", testPojoJson);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").process(new ObjectToJsonConverter(new ObjectMapper())).to("mock:result");
            }
        };
    }

}