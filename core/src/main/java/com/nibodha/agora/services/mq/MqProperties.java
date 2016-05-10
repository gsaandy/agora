/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 10/02/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "platform.mq")
public class MqProperties {


    private String brokerUrl = "vm://localhost:61616";
    private boolean enabled;
    private String userName;
    private String password;
    private String dataDir;
    private String deadLetterQueueName;
    private String queueNames;
    private String topicNames;
    private int queuePrefetchSize = 1;
    private int topicPrefetchSize = 1000;

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

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getDeadLetterQueueName() {
        return deadLetterQueueName;
    }

    public void setDeadLetterQueueName(String deadLetterQueueName) {
        this.deadLetterQueueName = deadLetterQueueName;
    }

    public String getQueueNames() {
        return queueNames;
    }

    public void setQueueNames(String queueNames) {
        this.queueNames = queueNames;
    }

    public String getTopicNames() {
        return topicNames;
    }

    public void setTopicNames(String topicNames) {
        this.topicNames = topicNames;
    }

    public int getQueuePrefetchSize() {
        return queuePrefetchSize;
    }

    public void setQueuePrefetchSize(int queuePrefetchSize) {
        this.queuePrefetchSize = queuePrefetchSize;
    }

    public int getTopicPrefetchSize() {
        return topicPrefetchSize;
    }

    public void setTopicPrefetchSize(int topicPrefetchSize) {
        this.topicPrefetchSize = topicPrefetchSize;
    }
}

