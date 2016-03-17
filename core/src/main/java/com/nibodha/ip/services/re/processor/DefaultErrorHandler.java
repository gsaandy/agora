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

import com.nibodha.ip.domain.ErrorInfo;
import com.nibodha.ip.domain.Message;
import com.nibodha.ip.exceptions.ExceptionType;
import com.nibodha.ip.exceptions.PlatformRuntimeException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import static com.nibodha.ip.services.re.processor.RoutingEngineErrorHandler.DEAD_LETTER_URI;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class DefaultErrorHandler extends AbstractErrorHandler<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    protected ErrorInfo handleException(Exception e) {
        ExceptionType type = PlatformRuntimeException.Type.GENERIC_FAILURE;
        if (e instanceof PlatformRuntimeException) {
            type = ((PlatformRuntimeException) e).getType();
        }
        int responseStatus = 500;
        if (e instanceof ClientErrorException) {
            final Response response = ((ClientErrorException) e).getResponse();
            responseStatus = response.getStatus();
        }
        final ErrorInfo errorInfo = new ErrorInfo(type, e.getMessage());
        errorInfo.setStatusCode(responseStatus);
        return errorInfo;

    }
}
