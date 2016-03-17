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

package com.nibodha.ip.services.re.components.rs;

import com.nibodha.ip.services.re.processor.RoutingEngineErrorHandler;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.jaxrs.CxfRsProducer;
import org.apache.commons.lang3.StringUtils;

/**
 * @author gibugeorge on 16/01/16.
 * @version 1.0
 */
public class RsProducer extends CxfRsProducer {
    private final RsEndpoint endpoint;

    public RsProducer(final RsEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(final Exchange exchange) throws Exception {
        final String deadLetterUri = endpoint.getDeadLetterUri();
        if (StringUtils.isNotEmpty(deadLetterUri)) {
            exchange.setProperty(RoutingEngineErrorHandler.DEAD_LETTER_URI, deadLetterUri);
        }
        super.process(exchange);
    }
}
