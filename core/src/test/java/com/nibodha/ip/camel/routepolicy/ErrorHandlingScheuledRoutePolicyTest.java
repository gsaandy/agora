/*
 * Copyright 2016 Nibodha Trechnologies Pvt. Ltd.
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

package com.nibodha.ip.camel.routepolicy;

import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.quartz2.QuartzComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
public class ErrorHandlingScheuledRoutePolicyTest extends CamelTestSupport {

    @Test
    public void whenCronIsNotOffRouteStarts() throws Exception {
        MockEndpoint endpoint = context.getEndpoint("mock:success", MockEndpoint.class);
        endpoint.expectedMessageCount(1);

        context.getComponent("quartz2", QuartzComponent.class);

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                ErrorHandlingScheuledRoutePolicy policy = new ErrorHandlingScheuledRoutePolicy();
                policy.setRouteStartTime("*/3 * * * * ?");

                from("direct:start")
                        .routeId("test")
                        .routePolicy(policy).autoStartup(false)
                        .to("mock:success");

            }
        });
        context.start();
        Thread.sleep(5000);
        assertTrue(context.getRouteStatus("test") == ServiceStatus.Started);

        template.sendBody("direct:start", "Ready or not, Here, I come");
        endpoint.assertIsSatisfied();

    }

    @Test
    public void whenCronIsOffRouteIsNotStarted() throws Exception {
        MockEndpoint endpoint = context.getEndpoint("mock:success", MockEndpoint.class);


        context.getComponent("quartz2", QuartzComponent.class);

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                ErrorHandlingScheuledRoutePolicy policy = new ErrorHandlingScheuledRoutePolicy();
                policy.setRouteStartTime("OFF");

                from("direct:start")
                        .routeId("test")
                        .routePolicy(policy).autoStartup(false)
                        .to("mock:success");

            }
        });
        context.start();
        Thread.sleep(5000);
        assertTrue(context.getRouteStatus("test") == ServiceStatus.Stopped);


    }


}