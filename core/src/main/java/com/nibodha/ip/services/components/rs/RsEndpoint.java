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

package com.nibodha.ip.services.components.rs;

import com.nibodha.ip.exceptions.PlatformRuntimeException;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.cxf.jaxrs.BindingStyle;
import org.apache.camel.component.cxf.jaxrs.CxfRsEndpoint;
import org.apache.camel.spi.UriEndpoint;

/**
 * @author gibugeorge on 16/01/16.
 * @version 1.0
 */
@UriEndpoint(scheme = "rs", title = "RS", syntax = "rs:beanId:address", consumerClass = RsConsumer.class, label = "rest")
public class RsEndpoint extends CxfRsEndpoint {


    private String deadLetterUri;

    public RsEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
        setAddress(endpointUri);
    }

    @Override
    public Consumer createConsumer(final Processor processor) {
        final RsConsumer answer = new RsConsumer(this, processor);
        try {
            configureConsumer(answer);
        } catch (Exception e) {
            throw new PlatformRuntimeException(PlatformRuntimeException.Type.ROUTING_ENGINE_RS_ENDPOINT_CONFIG, "Exception configuring RsConsumer", e);
        }
        return answer;
    }

    @Override
    public Producer createProducer() {
        if (getBindingStyle() == BindingStyle.SimpleConsumer) {
            throw new IllegalArgumentException("The SimpleConsumer Binding Style cannot be used in a camel-cxfrs producer");
        }
        return new RsProducer(this);
    }

    public void setDeadLetterUri(String deadLetterUri) {
        this.deadLetterUri = deadLetterUri;
    }

    public String getDeadLetterUri() {
        return deadLetterUri;
    }
}
