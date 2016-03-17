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

package com.nibodha.ip.services.re.components;

import org.apache.camel.test.AvailablePortFinder;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
public class AvailablePortsCache {
    static final int PORT1 = AvailablePortFinder.getNextAvailable();

    static {
        System.setProperty("AvailablePortsCache.port1", Integer.toString(PORT1));
    }

    private AvailablePortsCache() {
    }

    public static int getPort(String name) {
        int port = AvailablePortFinder.getNextAvailable();
        System.setProperty(name, Integer.toString(port));
        return port;
    }

    public static int getPort1() {
        return PORT1;
    }

}
