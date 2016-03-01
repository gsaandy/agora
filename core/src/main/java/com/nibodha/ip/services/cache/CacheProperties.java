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

package com.nibodha.ip.services.cache;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author gibugeorge on 25/02/16.
 * @version 1.0
 */
public class CacheProperties {

    @Value("${platform.cache.enabled:true}")
    private boolean enabled;
    @Value("${platform.cache.config.path:}")
    private String config;

    public boolean isEnabled() {
        return enabled;
    }

    public String getConfig() {
        return config;
    }
}