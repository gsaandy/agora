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

package com.nibodha.agora.services.re;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gibugeorge on 14/01/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/nip-test-context.xml"})
public class JavaDslRouteDefinitionsInjctorTest {

    @BeforeClass
    public static void setup() {
        System.setProperty("config.location", "classpath:");
    }
    @Autowired
    private RouteDefinitionsInjector routeDefinitionsInjector;

    @Test
    public void whenRouteContextIsAvailableRoutesAreInjectedToCamelContext() throws Exception{
        Assert.assertTrue(routeDefinitionsInjector.getCamelContext().getRoutes().size()>0);
    }
}

class JavaDslRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:test").to("mock:result");
    }
}
