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

package com.nibodha.ip.services.components.rs;

import com.nibodha.ip.services.components.AvailablePortsCache;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
public class RsProducerTest extends CamelSpringTestSupport{

    private static int port1 = AvailablePortsCache.getPort1();

    @Test
    public void testRsProducer() {
        Exchange exchange
                = template.send("rs://http://localhost:"+port1+"/rsTest?httpClientAPI=true", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.setPattern(ExchangePattern.InOut);
                Message inMessage = exchange.getIn();
                // set the Http method
                inMessage.setHeader(Exchange.HTTP_METHOD, "GET");
                // set the relative path
                inMessage.setHeader(Exchange.HTTP_PATH, "/testService/stringvalue/100/");
                // put the response's entity into out message body
                inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_RESPONSE_CLASS, String.class);

            }
        });
        final String response = exchange.getOut().getBody(String.class);
        assertNotNull(response);
    }
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/rs-spring-producer-test-context.xml");
    }
}
