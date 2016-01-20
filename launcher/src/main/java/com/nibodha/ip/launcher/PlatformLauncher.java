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
package com.nibodha.ip.launcher;

import com.nibodha.ip.camel.RouteDefinitionsInjector;
import com.nibodha.ip.services.config.PlatformConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@Import(PlatformConfiguration.class)
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
        final RouteDefinitionsInjector routeDefinitionsInjector = configurableApplicationContext.getBean(RouteDefinitionsInjector.class);
        routeDefinitionsInjector.inject();
    }


}
