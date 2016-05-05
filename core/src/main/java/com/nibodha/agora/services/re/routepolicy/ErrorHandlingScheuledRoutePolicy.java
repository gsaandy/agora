/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.re.routepolicy;

import org.apache.camel.Route;
import org.apache.camel.routepolicy.quartz2.CronScheduledRoutePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gibugeorge on 05/01/16.
 * @version 1.0
 */
public class ErrorHandlingScheuledRoutePolicy extends CronScheduledRoutePolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingScheuledRoutePolicy.class);

    @Override
    protected void doOnInit(final Route route) throws Exception {
        if(getRouteStartTime()!=null && !("OFF").equals(getRouteStartTime())) {
            super.doOnInit(route);
        }

    }

    @Override
    protected void onJobExecute(final Action action, final Route route) throws Exception {
        try {
            super.onJobExecute(action, route);
        } catch (Exception e) {
            LOGGER.error("Exception while running route " + route.getId(), e);
            route.getRouteContext().getCamelContext().stopRoute(route.getId());
        }
    }
}
