/*
 * Copyright 2016 Nibodha Trechnologies Pvt. Ltd.
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

import org.apache.camel.Processor;
import org.apache.camel.component.cxf.jaxrs.CxfRsConsumer;
import org.apache.camel.component.cxf.jaxrs.CxfRsEndpoint;

/**
 * @author gibugeorge on 16/01/16.
 * @version 1.0
 */
public class RsConsumer extends CxfRsConsumer {
    public RsConsumer(final CxfRsEndpoint endpoint, final Processor processor) {
        super(endpoint, processor);
    }
}
