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

import com.nibodha.agora.services.config.PlatformConfiguration;
import com.nibodha.agora.services.mq.PlatformMqProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.xbean.BrokerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * @author gibugeorge on 11/01/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "platform.mq", value = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(PlatformConfiguration.class)
@EnableConfigurationProperties(PlatformMqProperties.class)
public class PlatformMqConfiguration {

    @Autowired
    private PlatformMqProperties platformMqProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public PooledConnectionFactory pooledConnectionFactory(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        return pooledConnectionFactory;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        final ActiveMQConnectionFactory activeMQConnectionFactory;
        if (platformMqProperties.getUserName() != null && platformMqProperties.getPassword() != null) {
            activeMQConnectionFactory = new ActiveMQConnectionFactory(platformMqProperties.getUserName(), platformMqProperties.getPassword(), platformMqProperties.getBrokerUrl());
        } else {
            activeMQConnectionFactory = new ActiveMQConnectionFactory(platformMqProperties.getBrokerUrl());
        }
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Bean
    public BrokerFactoryBean brokerFactoryBean() throws Exception {
        return new BrokerFactoryBean(resourceLoader.getResource("classpath:META-INF/spring/mq-broker-configuration.xml"));

    }
}
