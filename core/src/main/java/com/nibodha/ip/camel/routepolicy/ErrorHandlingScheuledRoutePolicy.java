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

package com.nibodha.ip.camel.routepolicy;

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
