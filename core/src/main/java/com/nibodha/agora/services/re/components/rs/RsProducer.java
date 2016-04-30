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

package com.nibodha.agora.services.re.components.rs;

import com.nibodha.agora.services.audit.AuditContext;
import com.nibodha.agora.services.cxf.audit.CxfAuditInInterceptor;
import com.nibodha.agora.services.cxf.audit.CxfAuditOutInterceptor;
import com.nibodha.agora.services.re.processor.RoutingEngineErrorHandler;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.jaxrs.CxfRsProducer;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.util.List;

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
        addAuditInterceptors(exchange.getContext());
        super.process(exchange);
    }

    private void addAuditInterceptors(final CamelContext camelContext) {
        final List<Interceptor<? extends Message>> outInterceptors = this.endpoint.getOutInterceptors();
        if (!isInterceptorAlreadyAdded(outInterceptors, CxfAuditOutInterceptor.class)) {
            final AuditContext auditContext = new AuditContext(camelContext, Phase.PRE_STREAM);
            final CxfAuditOutInterceptor auditInterceptor = new CxfAuditOutInterceptor(auditContext);
            this.endpoint.getOutInterceptors().add(auditInterceptor);
        }
        final List<Interceptor<? extends Message>> inInterceptors = this.endpoint.getInInterceptors();
        if (!isInterceptorAlreadyAdded(inInterceptors, CxfAuditInInterceptor.class)) {
            final AuditContext auditContext = new AuditContext(camelContext, Phase.RECEIVE);
            final CxfAuditInInterceptor auditInterceptor = new CxfAuditInInterceptor(auditContext);
            this.endpoint.getInInterceptors().add(auditInterceptor);
        }
    }


    private boolean isInterceptorAlreadyAdded(final List<Interceptor<? extends Message>> interceptors, Class<?> type) {
        for (Interceptor<? extends Message> outInterceptor : interceptors) {
            if (outInterceptor.getClass().isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }


}
