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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author gibugeorge on 17/12/15.
 * @version 1.0
 */
@Component
public class RouteStopingProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteStopingProcessor.class);

    @Override
    public void process(final Exchange exchange) throws Exception {

        new Thread() {
            @Override
            public void run() {
                try {
                    final String routeId = exchange.getFromRouteId();
                    LOGGER.info("Stopping route " + routeId);
                    exchange.getContext().stopRoute(routeId);
                } catch (Exception e) {
                    // ignore
                }
            }
        }.start();
    }
}
