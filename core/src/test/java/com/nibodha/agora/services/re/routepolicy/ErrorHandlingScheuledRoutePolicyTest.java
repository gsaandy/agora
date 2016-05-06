/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.re.routepolicy;

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
