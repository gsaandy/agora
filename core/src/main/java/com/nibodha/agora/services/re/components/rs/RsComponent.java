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

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.cxf.jaxrs.CxfRsComponent;
import org.apache.camel.util.CamelContextHelper;
import org.apache.camel.util.CastUtils;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.AbstractJAXRSFactoryBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gibugeorge on 16/01/16.
 * @version 1.0
 */
public class RsComponent extends CxfRsComponent {

    public RsComponent(final CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception {
        RsEndpoint answer;


        if (remaining.startsWith(CxfConstants.SPRING_CONTEXT_ENDPOINT)) {
            // Get the bean from the Spring context
            String beanId = remaining.substring(CxfConstants.SPRING_CONTEXT_ENDPOINT.length());
            if (beanId.startsWith("//")) {
                beanId = beanId.substring(2);
            }

            final AbstractJAXRSFactoryBean bean = CamelContextHelper.mandatoryLookup(getCamelContext(), beanId,
                    AbstractJAXRSFactoryBean.class);
            answer = new RsSpringEndpoint(this, remaining, bean);
            if (bean.getProperties() != null) {
                Map<String, Object> copy = new HashMap<>();
                copy.putAll(bean.getProperties());
                setProperties(answer, copy);
            }

            answer.setBeanId(beanId);

        } else {
            // endpoint URI does not specify a bean
            answer = new RsEndpoint(remaining, this);
        }
        final String deadLetterUri = (String) parameters.get("deadLetterUri");
        if (StringUtils.isNotEmpty(deadLetterUri)) {
            answer.setDeadLetterUri(deadLetterUri);
        }
        final String resourceClass = getAndRemoveParameter(parameters, "resourceClass", String.class);
        if (StringUtils.isNotEmpty(resourceClass)) {
            Class<?> clazz = getCamelContext().getClassResolver().resolveMandatoryClass(resourceClass);
            answer.addResourceClass(clazz);
        }

        final String resourceClasses = getAndRemoveParameter(parameters, "resourceClasses", String.class);
        Iterator<?> it = ObjectHelper.createIterator(resourceClasses);
        while (it.hasNext()) {
            String name = (String) it.next();
            Class<?> clazz = getCamelContext().getClassResolver().resolveMandatoryClass(name);
            answer.addResourceClass(clazz);
        }

        setProperties(answer, parameters);
        final Map<String, String> params = CastUtils.cast(parameters);
        answer.setParameters(params);
        setEndpointHeaderFilterStrategy(answer);
        return answer;
    }
}
