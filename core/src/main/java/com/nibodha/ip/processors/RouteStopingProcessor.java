/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
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
public class RouteStopingProcessor implements Processor{

    private Thread stop;
    private static final Logger LOGGER= LoggerFactory.getLogger(RouteStopingProcessor.class);
    @Override
    public void process(final Exchange exchange) throws Exception {

        if(stop == null) {
            stop = new Thread() {
                @Override
                public void run() {
                    try {
                        final String routeId = exchange.getFromRouteId();
                        LOGGER.info("Stopping route "+routeId);
                        exchange.getContext().stopRoute(routeId);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            };
        }
        stop.start();
    }
}
