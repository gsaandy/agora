/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */
package com.nibodha.ip.launcher;

import com.nibodha.ip.camel.RouteDefinitionsInjector;
import com.nibodha.ip.config.PlatformConfiguration;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.management.ManagementFactory;
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

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            @Value("${server.port:8080}") final String mainPort) {
        final JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory(Integer.valueOf(mainPort));
        factory.addServerCustomizers(new JettyServerCustomizer() {
            @Override
            public void customize(final Server server) {
                // Expose Jetty managed beans to the JMX platform server provided by Spring
                final MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
                server.addBean(mbContainer);
            }
        });
        return factory;
    }

}
