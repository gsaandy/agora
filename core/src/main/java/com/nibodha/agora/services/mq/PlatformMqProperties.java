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

package com.nibodha.agora.services.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author gibugeorge on 10/02/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "platform.mq")
public class PlatformMqProperties {


    private String brokerUrl = "vm://localhost:61616";
    private boolean enabled;
    private String userName;
    private String password;
    private Resource dataDir;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Resource getDataDir() {
        return dataDir;
    }

    public void setDataDir(Resource dataDir) {
        this.dataDir = dataDir;
    }
}
