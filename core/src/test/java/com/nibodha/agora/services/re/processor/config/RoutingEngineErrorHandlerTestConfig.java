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

package com.nibodha.agora.services.re.processor.config;

import com.nibodha.agora.services.re.config.RoutingEngineConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
@Configuration
@Import(RoutingEngineConfiguration.class)
@ImportResource("classpath*:META-INF/spring/routing-engine-error-handler-test-context.xml")
public class RoutingEngineErrorHandlerTestConfig {

    @Bean
    public PooledConnectionFactory pooledConnectionFactory(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        return pooledConnectionFactory;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:76567");
    }
}
