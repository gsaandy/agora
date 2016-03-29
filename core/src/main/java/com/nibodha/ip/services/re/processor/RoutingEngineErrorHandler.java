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

import org.apache.camel.*;
import org.apache.camel.processor.DeadLetterChannel;
import org.apache.camel.processor.RedeliveryPolicy;
import org.apache.camel.processor.exceptionpolicy.ExceptionPolicyStrategy;
import org.apache.camel.spring.spi.TransactionErrorHandler;
import org.apache.camel.util.CamelLogger;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class RoutingEngineErrorHandler extends DeadLetterChannel {

    private final TransactionErrorHandler transactionErrorHandler;

    public static final String DEAD_LETTER_URI = "DEAD_LETTER_URI";

    public RoutingEngineErrorHandler(final CamelContext camelContext, final Processor output, final CamelLogger logger,
                                     final Processor redeliveryProcessor, final RedeliveryPolicy redeliveryPolicy,
                                     final ExceptionPolicyStrategy exceptionPolicyStrategy, final Processor deadLetter,
                                     final String deadLetterUri, final boolean deadLetterHandleException,
                                     final boolean useOriginalBodyPolicy, final Predicate retryWhile,
                                     final ScheduledExecutorService executorService, final Processor onPrepareProcessor,
                                     final Processor onExceptionOccurredProcessor,
                                     final TransactionErrorHandler transactionErrorHandler) {
        super(camelContext, output, logger, redeliveryProcessor, redeliveryPolicy, exceptionPolicyStrategy, deadLetter,
                deadLetterUri, deadLetterHandleException, useOriginalBodyPolicy, retryWhile, executorService,
                onPrepareProcessor, onExceptionOccurredProcessor);
        this.transactionErrorHandler = transactionErrorHandler;
    }


    /**
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(final Exchange exchange) throws Exception {

        if (exchange.isTransacted()) {
            this.transactionErrorHandler.process(exchange);
        } else {
            super.process(exchange);
        }
    }

    /**
     * @param exchange
     * @param callback
     * @return
     */
    @Override
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        if (exchange.isTransacted()) {
            return this.transactionErrorHandler.process(exchange, callback);
        } else {
            return super.process(exchange, callback);
        }
    }

    public TransactionErrorHandler getTransactionErrorHandler() {
        return transactionErrorHandler;
    }
}
