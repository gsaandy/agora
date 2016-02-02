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

package com.nibodha.ip.services.camel.spring;

import com.nibodha.ip.services.camel.spring.spi.PlatformPropertyPlaceholderConfigurer;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

/**
 * @author gibugeorge on 28/01/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/directory-watcher-test-context.xml"})
public class ConfigurationDirectoryWatcherTest {

    static {
        System.setProperty("config.location", ".");
    }

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ConfigurationDirectoryWatcher configurationDirectoryWatcher;

    @Autowired
    private PlatformPropertyPlaceholderConfigurer propertyPlaceholderConfigurer;

    private final Properties properties = new Properties();

    private Resource resource;


    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationDirectoryWatcherTest.class);


    @Before
    public void setUp() {
        configurationDirectoryWatcher.start();
        resource = resourceLoader.getResource(System.getProperty("config.location"));
    }

    @Test
    public void whenPropertiesAreModifiedPlaceHolderConfigurerIsRefreshed() throws Exception {
        final Properties properties = new Properties();
        properties.load(new FileReader(resource.getFile().getAbsolutePath() + "/test-watcher.properties"));
        properties.setProperty("to.modify", "modified");
        properties.store(new FileWriter(resource.getFile().getAbsolutePath() + "/test-watcher.properties"), "");
        Thread.sleep(10000);
        Assert.assertEquals("modified", this.propertyPlaceholderConfigurer.getProperties().getProperty("to.modify"));

    }


}
