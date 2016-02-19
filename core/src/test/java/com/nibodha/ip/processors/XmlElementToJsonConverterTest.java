/*
 * Copyright 2016 Nibodha Technologies Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nibodha.ip.processors;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author gibugeorge on 14/01/16.
 * @version 1.0
 */
public class XmlElementToJsonConverterTest extends CamelTestSupport {

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
    public void whenInputElementIsNullNullIsReturned() throws InterruptedException {
        template.send(exchange);
        assertNull(exchange.getIn().getBody());
    }

    @Test
    public void whenInputElementHasLineBreaksJsonStringIsReturned() throws Exception {
        final String xmlString = "<tests>\n\r  " +
                "   <test>1234</test>\n\r   " +
                "</tests>  ";
        exchange.getIn().setBody(xmlString);
        template.send(exchange);
        assertEquals("{\"tests\":{\"test\":1234}}", exchange.getIn().getBody());
    }

    @Test
    public void whenInputElementHasAttributeProperJsonStringIsReturned() {
        final String xmlString = "<tests>\n\r  " +
                "   <test id=\"1\">1234</test>\n\r   " +
                "</tests>  ";
        exchange.getIn().setBody(xmlString);
        template.send(exchange);
        assertEquals("{\"tests\":{\"test\":{\"id\":\"1\",\"value\":\"1234\"}}}", exchange.getIn().getBody());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").process(new XmlElementToJsonConverter()).to("mock:result");
            }
        };
    }

}
