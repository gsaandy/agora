/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.mq.config;

import com.nibodha.agora.services.config.PlatformConfiguration;
import com.nibodha.agora.services.mq.MqProperties;
import com.nibodha.agora.services.mq.factory.BrokerFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.SharedDeadLetterStrategy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.activemq.xbean.XBeanBrokerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gibugeorge on 11/01/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "platform.mq", value = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(PlatformConfiguration.class)
@EnableConfigurationProperties(MqProperties.class)
public class MqConfiguration {

    @Inject
    private MqProperties mqProperties;

    private static final String MQ_BROKER_NAME = "mqBroker";


    @Bean
    public PooledConnectionFactory pooledConnectionFactory(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        return pooledConnectionFactory;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        final ActiveMQConnectionFactory activeMQConnectionFactory;
        if (mqProperties.getUserName() != null && mqProperties.getPassword() != null) {
            activeMQConnectionFactory = new ActiveMQConnectionFactory(mqProperties.getUserName(), mqProperties.getPassword(), mqProperties.getBrokerUrl());
        } else {
            activeMQConnectionFactory = new ActiveMQConnectionFactory(mqProperties.getBrokerUrl());
        }
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Bean
    public BrokerFactory brokerFactory(final XBeanBrokerService broker) throws Exception {
        final BrokerFactory brokerFactory = new BrokerFactory(broker);
        return brokerFactory;

    }

    @Bean
    public XBeanBrokerService broker() throws Exception {
        final XBeanBrokerService brokerService = new XBeanBrokerService();
        brokerService.setBrokerName(MQ_BROKER_NAME);
        brokerService.setUseShutdownHook(true);
        brokerService.setUseJmx(true);
        brokerService.setManagementContext(getManagementContext());
        brokerService.setPersistenceAdapter(getPersistenceAdapter());
        brokerService.getTransportConnectors().add(getTransportConnector());

        final PolicyMap policyMap = new PolicyMap();
        policyMap.setPolicyEntries(getPolicyEntries());
        brokerService.setDestinationPolicy(policyMap);

        final List<ActiveMQDestination> destinationsList = new ArrayList<>();
        destinationsList.addAll(createQueues());
        destinationsList.addAll(createTopics());
        brokerService.setDestinations(destinationsList.toArray(new ActiveMQDestination[destinationsList.size()]));
        return brokerService;
    }

    private List<PolicyEntry> getPolicyEntries() {
        final PolicyEntry queuePolicy = new PolicyEntry();
        queuePolicy.setQueue(">");
        queuePolicy.setQueuePrefetch(1);
        if (StringUtils.isNotEmpty(mqProperties.getDeadLetterQueueName())) {
            final SharedDeadLetterStrategy deadLetterStrategy = new SharedDeadLetterStrategy();
            deadLetterStrategy.setDeadLetterQueue(new ActiveMQQueue(mqProperties.getDeadLetterQueueName()));
            queuePolicy.setDeadLetterStrategy(deadLetterStrategy);
        }
        final PolicyEntry topicPolicy = new PolicyEntry();
        topicPolicy.setTopic(">");
        topicPolicy.setTopicPrefetch(1000);
        final List<PolicyEntry> policyEntries = new ArrayList<>();
        policyEntries.add(queuePolicy);
        policyEntries.add(topicPolicy);
        return policyEntries;

    }

    private List<ActiveMQQueue> createQueues() {
        final List<ActiveMQQueue> destinations = new ArrayList<>();
        if (StringUtils.isNotEmpty(mqProperties.getQueueNames())) {
            final String[] queueNames = mqProperties.getQueueNames().split(",");
            for (final String queueName : queueNames) {
                destinations.add(new ActiveMQQueue(queueName));
            }
        }
        return destinations;
    }

    private List<ActiveMQTopic> createTopics() {
        final List<ActiveMQTopic> destinations = new ArrayList<>();
        if (StringUtils.isNotEmpty(mqProperties.getTopicNames())) {
            final String[] topicNames = mqProperties.getTopicNames().split(",");
            for (final String topicName : topicNames) {
                destinations.add(new ActiveMQTopic(topicName));
            }
        }
        return destinations;
    }


    private TransportConnector getTransportConnector() throws URISyntaxException {
        final TransportConnector transportConnector = new TransportConnector();
        transportConnector.setName("platformMqConnector");
        transportConnector.setUri(new URI(mqProperties.getBrokerUrl() + "?wireFormat.maxInactivityDurationInitalDelay=30000"));
        return transportConnector;
    }

    private PersistenceAdapter getPersistenceAdapter() throws IOException {
        final PersistenceAdapter persistenceAdapter = new KahaDBPersistenceAdapter();
        File dataDir = new File(System.getProperty("java.io.tmpdir"));
        if (StringUtils.isNotEmpty(mqProperties.getDataDir())) {
            dataDir = new File(mqProperties.getDataDir());
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
        }
        persistenceAdapter.setDirectory(dataDir);
        persistenceAdapter.setBrokerName(MQ_BROKER_NAME);
        return persistenceAdapter;
    }

    private ManagementContext getManagementContext() {
        final ManagementContext managementContext = new ManagementContext();
        managementContext.setJmxDomainName("com.nibodha.ip");
        managementContext.setUseMBeanServer(true);
        managementContext.setCreateConnector(false);
        return managementContext;
    }


}

