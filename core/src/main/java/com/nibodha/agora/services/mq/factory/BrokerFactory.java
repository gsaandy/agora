/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.mq.factory;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.xbean.BrokerFactoryBean;
import org.apache.activemq.xbean.XBeanBrokerService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;


/**
 * @author gibugeorge on 18/03/16.
 * @version 1.0
 */
public class BrokerFactory extends BrokerFactoryBean {

    private ApplicationContext applicationContext;
    private final XBeanBrokerService broker;

    public BrokerFactory(final XBeanBrokerService brokerService) {
        this.broker = brokerService;
    }

    @Override
    public void destroy() throws Exception {
        if (broker != null) {
            broker.stop();
        }
    }

    @Override
    public Object getObject() throws Exception {
        return broker;
    }

    @Override
    public Class<?> getObjectType() {
        return BrokerService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isSystemExitOnShutdown()) {
            broker.addShutdownHook(new Runnable() {
                public void run() {
                    System.exit(getSystemExitOnShutdownExitCode());
                }
            });
        }
        if (isStart()) {
            broker.start();
        }
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public BrokerService getBroker() {
        return broker;
    }


}

