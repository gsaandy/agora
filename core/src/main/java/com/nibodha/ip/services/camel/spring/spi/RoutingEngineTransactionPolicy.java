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

package com.nibodha.ip.services.camel.spring.spi;

import com.nibodha.ip.services.camel.processor.RoutingEngineErrorHandler;
import org.apache.camel.Processor;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.camel.spring.spi.TransactionErrorHandler;
import org.apache.camel.util.ObjectHelper;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class RoutingEngineTransactionPolicy extends SpringTransactionPolicy {

    protected TransactionErrorHandler createTransactionErrorHandler(final RouteContext routeContext, final Processor processor, final ErrorHandlerBuilder builder) {
        RoutingEngineErrorHandler answer;
        try {
            answer = (RoutingEngineErrorHandler) builder.createErrorHandler(routeContext, processor);
        } catch (Exception e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
        return answer.getTransactionErrorHandler();
    }
}
