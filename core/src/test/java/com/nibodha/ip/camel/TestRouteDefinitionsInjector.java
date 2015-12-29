/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.ip.camel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gibugeorge on 24/12/15.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/nip-test-context.xml"})
public class TestRouteDefinitionsInjector {

    static {
        System.setProperty("config.location","classpath:.");
    }

    @Autowired
    private RouteDefinitionsInjector routeDefinitionsInjector;

    @Test
    public void whenRouteContextIsAvailableRoutesAreInjectedToCamelContext() {
        routeDefinitionsInjector.inject();
        Assert.assertTrue(routeDefinitionsInjector.getCamelContext().getRoutes().size()>0);
    }

}


