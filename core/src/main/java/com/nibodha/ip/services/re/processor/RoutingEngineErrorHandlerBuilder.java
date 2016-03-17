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

import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.spring.spi.TransactionErrorHandler;
import org.apache.camel.spring.spi.TransactionErrorHandlerBuilder;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class RoutingEngineErrorHandlerBuilder extends DeadLetterChannelBuilder {

    private final TransactionErrorHandlerBuilder transactionErrorHandlerBuilder;

    /**
     * @param transactionErrorHandlerBuilder
     */
    public RoutingEngineErrorHandlerBuilder(final TransactionErrorHandlerBuilder transactionErrorHandlerBuilder) {
        this.transactionErrorHandlerBuilder = transactionErrorHandlerBuilder;
    }

    /**
     *
     * @param routeContext
     * @param processor
     * @return
     * @throws Exception
     */
    @Override
    public Processor createErrorHandler(final RouteContext routeContext, final Processor processor) throws Exception {
        final TransactionErrorHandler transactionErrorHandler = (TransactionErrorHandler) transactionErrorHandlerBuilder.createErrorHandler(routeContext, processor);
        validateDeadLetterUri(routeContext);

        final RoutingEngineErrorHandler answer = new RoutingEngineErrorHandler(routeContext.getCamelContext(),
                processor, getLogger(), getOnRedelivery(), getRedeliveryPolicy(), getExceptionPolicyStrategy(),
                getFailureProcessor(), getDeadLetterUri(), isDeadLetterHandleNewException(), isUseOriginalMessage(),
                getRetryWhilePolicy(routeContext.getCamelContext()), getExecutorService(routeContext.getCamelContext()),
                getOnPrepareFailure(), transactionErrorHandler);
        // configure error handler before we can use it
        configure(routeContext, answer);

        return answer;
    }

    /**
     *
     * @return
     */
    public TransactionTemplate getTransactionTemplate() {
        return this.transactionErrorHandlerBuilder.getTransactionTemplate();
    }

    /**
     * Always returns true as this the error handler always support transaction
     * @return
     */
    @Override
    public boolean supportTransacted() {
        return true;
    }

    /**
     * Returns the rollback logginh level
     * @return
     */
    public LoggingLevel getRollbackLoggingLevel() {
        return this.transactionErrorHandlerBuilder.getRollbackLoggingLevel();
    }
}
