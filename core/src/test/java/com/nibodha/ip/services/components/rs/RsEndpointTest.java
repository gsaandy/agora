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

import com.nibodha.ip.services.components.rs.test.TestService;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
public class RsEndpointTest extends CamelTestSupport {

    @Test
    public void testCreateRsEndpoint() throws Exception {
        final String endpointUri = "cxfrs://http://localhost:" + AvailablePortFinder.getNextAvailable() + "/rsTest";
        RsComponent component = new RsComponent(context);
        RsEndpoint endpoint = (RsEndpoint) component.createEndpoint(endpointUri);

        assertNotNull("The endpoint should not be null ", endpoint);
        assertEquals(endpointUri, endpoint.getEndpointUri());
    }

    @Test
    public void testCreateRsEndpointWithWithResourceClass() throws Exception{
        final String endpointUri = "cxfrs://http://localhost:" + AvailablePortFinder.getNextAvailable() + "/rsTest?&resourceClass=com.nibodha.ip.services.components.rs.test.TestService";
        RsComponent component = new RsComponent(context);
        RsEndpoint endpoint = (RsEndpoint) component.createEndpoint(endpointUri);
        assertEquals(TestService.class,endpoint.getResourceClasses().get(0));

    }

    @Test
    public void testCreateRsEndpointWithWithResourceClasses() throws Exception{
        final String endpointUri = "cxfrs://http://localhost:" + AvailablePortFinder.getNextAvailable() + "/rsTest?&resourceClasses=com.nibodha.ip.services.components.rs.test.TestService,java.lang.String";
        RsComponent component = new RsComponent(context);
        RsEndpoint endpoint = (RsEndpoint) component.createEndpoint(endpointUri);
        assertEquals(2, endpoint.getResourceClasses().size());
        assertEquals(TestService.class,endpoint.getResourceClasses().get(0));

    }

}
