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

import org.apache.camel.component.cxf.spring.SpringJAXRSClientFactoryBean;
import org.apache.camel.component.cxf.spring.SpringJAXRSServerFactoryBean;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
public class RsSpringEndpointTest extends CamelSpringTestSupport {
    @Test
    public void testCreateCxfRsServerFactoryBean() {
        RsEndpoint endpoint = resolveMandatoryEndpoint("rs://bean:rsServer", RsEndpoint.class);
        SpringJAXRSServerFactoryBean sfb = (SpringJAXRSServerFactoryBean) endpoint.createJAXRSServerFactoryBean();
        assertEquals(sfb.getBeanId(), "rsServer");
        assertEquals(sfb.getAddress(), "http://localhost:9000/rsTest");

    }

    @Test
    public void testCreateCxfRsClientFactoryBean() {
        RsEndpoint endpoint = resolveMandatoryEndpoint("rs://bean:rsClient", RsEndpoint.class);
        SpringJAXRSClientFactoryBean cfb = (SpringJAXRSClientFactoryBean) endpoint.createJAXRSClientFactoryBean();
        assertEquals(cfb.getBeanId(), "rsClient");
        assertEquals(cfb.getAddress(), "http://localhost:9002/test");


    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/rs-spring-endpoint-test-context.xml");
    }
}
