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

package com.nibodha.ip.services.camel.config;

import com.nibodha.ip.services.mq.config.PlatformMqConfiguration;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.connection.JmsTransactionManager;

/**
 * @author gibugeorge on 02/02/16.
 * @version 1.0
 */
@Configuration
@ImportResource("classpath:META-INF/spring/nip-camel-context.xml")
@ConditionalOnProperty(prefix = "platform.routingengine", value = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(PlatformMqConfiguration.class)
public class CamelConfiguration {

    @Bean(name = "activemq")
    public ActiveMQComponent activeMQComponent(final PooledConnectionFactory pooledConnectionFactory) {
        final ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(pooledConnectionFactory);
        activeMQComponent.setTransacted(true);
        activeMQComponent.setTransactionManager(new JmsTransactionManager(pooledConnectionFactory));
        return activeMQComponent;
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager(final PooledConnectionFactory pooledConnectionFactory) {
        return new JmsTransactionManager(pooledConnectionFactory);
    }
}
