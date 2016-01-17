/*
 * Copyright 2016 Nibodha Trechnologies Pvt. Ltd.
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

package com.nibodha.ip.services.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author gibugeorge on 17/01/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {PropertyPlaceholderAutoConfiguration.class, EmbeddedContainerConfiguration.class})
public class EmbeddedContainerConfigurationTest {

    static {
        System.setProperty("jetty.config.path","./src/test/resources/jetty-config.xml");
        System.setProperty("server.port","7778");
    }
    @Autowired
    private JettyEmbeddedServletContainerFactory embeddedServletContainerFactory;

    @Test
    public void testEmbeddedServletContainerFactory() {
        final EmbeddedServletContainer embeddedServletContainer = embeddedServletContainerFactory.getEmbeddedServletContainer();
        embeddedServletContainer.start();
        Assert.assertEquals(7778,embeddedServletContainer.getPort());
        embeddedServletContainer.stop();

    }
}