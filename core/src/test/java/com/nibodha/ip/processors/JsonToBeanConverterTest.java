package com.nibodha.ip.processors;

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

import java.util.List;

public class JsonToBeanConverterTest extends CamelTestSupport {

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
    public void convertJsonObject() {
        final String jsonMessage = "{\"code\": \"Code\", \"value\": \"Value\"}";
        exchange.getIn().setBody(jsonMessage);
        exchange.getIn().setHeader("ConvertJsonToBeanType", "com.nibodha.ip.processors.TestPojo");
        template.send(exchange);
        final TestPojo testPojo = exchange.getIn().getBody(TestPojo.class);
        assertNotNull(testPojo);
        assertEquals("Code", testPojo.getCode());
        assertEquals("Value", testPojo.getValue());
    }

    @Test
    public void convertJsonCollection() {
        final String jsonMessage = "[{\"code\": \"Code1\", \"value\": \"Value1\"}, {\"code\": \"Code2\", \"value\": \"Value2\"}]";
        exchange.getIn().setBody(jsonMessage);
        exchange.getIn().setHeader("ConvertJsonToBeanType", "com.nibodha.ip.processors.TestPojo");
        exchange.getIn().setHeader("UseJsonCollection", true);
        template.send(exchange);
        final List<TestPojo> testPojos = exchange.getIn().getBody(List.class);
        assertNotNull(testPojos);
        assertCollectionSize(testPojos, 2);

        assertEquals("Code1", testPojos.get(0).getCode());
        assertEquals("Value1", testPojos.get(0).getValue());

        assertEquals("Code2", testPojos.get(1).getCode());
        assertEquals("Value2", testPojos.get(1).getValue());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").process(new JsonToBeanConverter(new ObjectMapper())).to("mock:result");
            }
        };
    }

}