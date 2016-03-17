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

package com.nibodha.ip.services.re.config;

import com.nibodha.ip.services.mq.config.PlatformMqConfiguration;
import com.nibodha.ip.services.re.RoutingEngineProperties;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.processor.interceptor.Tracer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.connection.JmsTransactionManager;

import javax.inject.Inject;

/**
 * @author gibugeorge on 02/02/16.
 * @version 1.0
 */
@Configuration
@ImportResource("classpath:META-INF/spring/nip-camel-context.xml")
@ConditionalOnProperty(prefix = "platform.routingengine", value = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(PlatformMqConfiguration.class)
@EnableConfigurationProperties(RoutingEngineProperties.class)
public class RoutingEngineConfiguration {

    @Inject
    private RoutingEngineProperties routingEngineProperties;

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

    @Bean
    @ConditionalOnProperty(prefix = "platform.routingengine", value = "trace-enabled", havingValue = "true", matchIfMissing = false)
    public Tracer routingEngineTracer() {
        final Tracer tracer = new Tracer();
        tracer.setEnabled(routingEngineProperties.isTraceEnabled());
        tracer.setTraceExceptions(routingEngineProperties.isTaceExceptions());
        tracer.setTraceInterceptors(routingEngineProperties.isTraceInterceptors());
        tracer.setLogLevel(routingEngineProperties.getLogLevel());
        tracer.setLogName(routingEngineProperties.getLogName());
        return tracer;
    }
}
