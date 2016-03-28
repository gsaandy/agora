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

package com.nibodha.ip.services.cxf.audit;

import com.nibodha.ip.domain.AuditInfo;
import com.nibodha.ip.services.audit.AuditContext;
import com.nibodha.ip.services.audit.AuditInterceptor;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

/**
 * @author gibugeorge on 28/03/16.
 * @version 1.0
 */
public abstract class AbstractAuditInterceptor extends AbstractPhaseInterceptor<Message> implements AuditInterceptor {

    private final AuditContext auditContext;
    private static final String AUDIT_INFO_QUEUE = "activemq://queue:audit-info-queue";

    public AbstractAuditInterceptor(final AuditContext auditContext) {
        super(auditContext.getPhase());
        this.auditContext = auditContext;
    }

    @Override
    public void audit(AuditInfo auditInfo) {
        final ProducerTemplate producerTemplate = auditContext.getCamelContext().createProducerTemplate();
        final Exchange exchange = new DefaultExchange(auditContext.getCamelContext(), ExchangePattern.InOnly);
        exchange.getIn().setBody(auditInfo.toString());
        producerTemplate.asyncSend(AUDIT_INFO_QUEUE, exchange);
    }

}
