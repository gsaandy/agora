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

package com.nibodha.ip.services.camel.processor;

import com.nibodha.ip.domain.Error;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class DefaultErrorHandler implements Processor {

    @Override
    public void process(final Exchange exchange) throws Exception {

        final Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        final String errorCode = exchange.getIn().getHeader("ERROR_CODE", String.class);
        final String errorType = exchange.getIn().getHeader("ERROR_TYPE", String.class);
        final Error error = new Error(exception, errorCode, errorType);
        exchange.getIn().setBody(error);
    }
}
