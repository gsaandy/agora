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

package com.nibodha.ip.camel.management;

import org.apache.camel.Exchange;
import org.apache.camel.management.event.ExchangeCompletedEvent;
import org.apache.camel.management.event.ExchangeSentEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.EventObject;

/**
 * @author gibugeorge on 16/12/15.
 * @version 1.0
 */
@Component
public class AuditEventNotifier extends EventNotifierSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventNotifier.class);

    @Override
    public void notify(final EventObject event) throws Exception {
        if (event instanceof ExchangeSentEvent) {
            final ExchangeSentEvent sent = (ExchangeSentEvent) event;
            LOGGER.info(">>> Took " + sent.getTimeTaken() + " millis to send to external system : " + sent.getEndpoint());
        }

        if (event instanceof ExchangeCompletedEvent) {
            final ExchangeCompletedEvent exchangeCompletedEvent = (ExchangeCompletedEvent) event;
            final Exchange exchange = exchangeCompletedEvent.getExchange();
            final String routeId = exchange.getFromRouteId();
            final Date created = ((ExchangeCompletedEvent) event).getExchange().getProperty(Exchange.CREATED_TIMESTAMP, Date.class);
            // calculate elapsed time
            final Date now = new Date();
            final long elapsed = now.getTime() - created.getTime();
            LOGGER.info(">>> Took " + elapsed + " millis for the exchange on the route : " + routeId);
        }
    }

    @Override
    public boolean isEnabled(EventObject event) {
        return false;
    }

    @Override
    protected void doStart() throws Exception {
        // filter out unwanted events
        setIgnoreCamelContextEvents(false);
        setIgnoreServiceEvents(false);
        setIgnoreRouteEvents(true);
        setIgnoreExchangeCreatedEvent(false);
        setIgnoreExchangeCompletedEvent(false);
        setIgnoreExchangeFailedEvents(false);
        setIgnoreExchangeRedeliveryEvents(true);
        setIgnoreExchangeSentEvents(false);
    }

}
