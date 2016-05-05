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

package com.nibodha.agora.services.mq.config;

import com.nibodha.agora.services.mq.MqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 01/03/16.
 * @version 1.0
 */
@Configuration
public class PlatformMqTestPropertiesConfiguration {

    @Bean
    public MqProperties mqProperties() {
        final MqProperties mqProperties = new MqProperties();
        mqProperties.setEnabled(true);
        mqProperties.setDataDir(System.getProperty("java.io.tmpdir"));
        return mqProperties;

    }
}
