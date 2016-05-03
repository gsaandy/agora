/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.launcher;

import com.nibodha.agora.env.PlatformEnvironment;
import com.nibodha.agora.services.file.AbstractDirectoryWatcher;
import com.nibodha.agora.services.jdbc.config.DatasourceConfiguration;
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
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@EnableAutoConfiguration(exclude = {PropertyPlaceholderAutoConfiguration.class, BatchAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, ActiveMQAutoConfiguration.class,
        MongoDataAutoConfiguration.class, CacheAutoConfiguration.class, JmsAutoConfiguration.class, DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class, ThymeleafAutoConfiguration.class
})

public class PlatformLauncher {

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
        final ConfigurableEnvironment environment = new PlatformEnvironment();

        application.setEnvironment(environment);
        application.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(final ConfigurableApplicationContext applicationContext) {
                applicationContext.addBeanFactoryPostProcessor(new DatasourceConfiguration(applicationContext.getEnvironment()));
            }
        });
        application.setRegisterShutdownHook(true);
        application.setWebEnvironment(true);
        application.setLogStartupInfo(true);
        final ConfigurableApplicationContext configurableApplicationContext = application.run(args);
        final Map<String, AbstractDirectoryWatcher> directoryWatchers = configurableApplicationContext.getBeansOfType(AbstractDirectoryWatcher.class);
        final Set<String> keySet = directoryWatchers.keySet();
        for (final String key : keySet) {
            AbstractDirectoryWatcher abstractDirectoryWatcher = directoryWatchers.get(key);
            abstractDirectoryWatcher.start();
        }


    }


}
