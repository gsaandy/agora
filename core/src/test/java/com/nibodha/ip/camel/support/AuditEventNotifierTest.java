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

package com.nibodha.ip.camel.support;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
public class AuditEventNotifierTest extends CamelTestSupport{


    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testAuditEventNotifier() throws Exception {
        resultEndpoint.expectedMessageCount(1);
        final AuditEventNotifier auditEventNotifier = new AuditEventNotifier();
        context().getManagementStrategy().addEventNotifier(auditEventNotifier);
        auditEventNotifier.start();
        template.sendBody(resultEndpoint, "body");
        assertMockEndpointsSatisfied();
    }

}