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

package com.nibodha.ip.services.camel.processor;

import com.nibodha.ip.domain.*;
import com.nibodha.ip.domain.Message;
import com.nibodha.ip.exceptions.PlatformRuntimeException;
import com.nibodha.ip.services.camel.processor.config.RoutingEngineErrorHandlerTestConfig;
import com.nibodha.ip.services.config.PlatformPlaceHolderConfiguration;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.language.ConstantExpression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {PlatformPlaceHolderConfiguration.class, RoutingEngineErrorHandlerTestConfig.class})
public class RoutingEngineErrorHandlerTest {

    static {
        System.setProperty("config.location", "classpath:.");
    }

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    protected Exchange exchange;

    @Autowired
    private CamelContext camelContext;

    @Before
    public void setUp() throws Exception {
        exchange = new DefaultExchange(camelContext);
    }


    @Test
    public void whenExceptionIsThrownTheExceptionIsWrappedInErrorObject() {
        template.send(exchange);
        final Message message = exchange.getIn().getBody(Message.class);
        Assert.assertNotNull(message.getErrorInfo());
        Assert.assertTrue(message.getErrorInfo().getType() == PlatformRuntimeException.Type.GENERIC_FAILURE);

    }


}

class ExceptionThrowingRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start").id("com.nibodha.ip.test.exceptionhandling").throwException(new RuntimeCamelException("error")).to("mock:result");
    }
}

