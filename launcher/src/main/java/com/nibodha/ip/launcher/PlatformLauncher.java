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
package com.nibodha.ip.launcher;

import com.nibodha.ip.services.camel.spring.ConfigurationDirectoryWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@EnableAutoConfiguration(exclude = {PropertyPlaceholderAutoConfiguration.class, BatchAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, ActiveMQAutoConfiguration.class,
        MongoDataAutoConfiguration.class, CacheAutoConfiguration.class, JmsAutoConfiguration.class
})

public class PlatformLauncher extends SpringBootServletInitializer {


    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformLauncher.class);

    public static void main(final String[] args) {

        printSystemProperties();
        new PlatformLauncher().run(args);

    }

    private static void printSystemProperties() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Java System Properties");
            final Properties properties = System.getProperties();
            final Enumeration<String> names = (Enumeration<String>) properties.propertyNames();
            while (names.hasMoreElements()) {
                final String name = names.nextElement();
                LOGGER.info(name + " = " + properties.getProperty(name));
            }
        }
    }

    public void run(final String[] args) {
        final SpringApplication application = new SpringApplication(PlatformLauncher.class);
        application.setRegisterShutdownHook(true);
        application.setWebEnvironment(true);
        application.setLogStartupInfo(true);
        final ConfigurableApplicationContext configurableApplicationContext = application.run(args);
        final ConfigurationDirectoryWatcher configurationDirectoryWatcher = configurableApplicationContext.getBean(ConfigurationDirectoryWatcher.class);
        configurationDirectoryWatcher.start();
    }


}
