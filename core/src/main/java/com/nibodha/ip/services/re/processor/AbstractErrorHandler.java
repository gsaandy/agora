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

package com.nibodha.ip.services.re.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nibodha.ip.domain.ErrorInfo;
import com.nibodha.ip.domain.Message;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.nibodha.ip.services.re.processor.RoutingEngineErrorHandler.DEAD_LETTER_URI;

/**
 * @author gibugeorge on 16/03/16.
 * @version 1.0
 */
public abstract class AbstractErrorHandler<E extends Exception> implements Processor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractErrorHandler.class);

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        LOGGER.error("Exception in route {}", exchange.getFromRouteId());
        LOGGER.error("Exception is ", e);
        final ErrorInfo errorInfo = this.handleException((E) e);
        exchange.removeProperty(Exchange.EXCEPTION_CAUGHT);
        final String deadLetterUri = exchange.getProperty(DEAD_LETTER_URI, String.class);
        if (StringUtils.isNotEmpty(deadLetterUri)) {
            exchange.removeProperty(DEAD_LETTER_URI);
            final ProducerTemplate producerTemplate = new DefaultProducerTemplate(exchange.getContext());
            producerTemplate.start();
            producerTemplate.send(deadLetterUri, exchange);
            return;
        }
        if (errorInfo != null) {
            final Message<Object> message = new Message<Object>(exchange.getIn().getHeader("Endpoint", String.class), errorInfo, exchange.getIn().getHeaders());
            exchange.getIn().setBody(message);
        }
    }

    protected abstract ErrorInfo handleException(E e);

}
