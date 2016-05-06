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

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.xbean.BrokerFactoryBean;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.SQLException;

/**
 * @author gibugeorge on 01/03/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {PlatformMqTestPropertiesConfiguration.class, MqConfiguration.class})
public class MqConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;


    @BeforeClass
    public static void setup() {
        System.setProperty("config.location", "classpath:");
    }

    @Test
    public void testMqConfiguration() {
        Assert.assertNotNull(applicationContext.getBean(PooledConnectionFactory.class));
        final BrokerFactoryBean brokerFactoryBean = applicationContext.getBean(BrokerFactoryBean.class);
        Assert.assertEquals("mqBroker", brokerFactoryBean.getBroker().getBrokerName());
        Assert.assertEquals("vm://mqBroker", brokerFactoryBean.getBroker().getVmConnectorURI().toString());

    }
}
