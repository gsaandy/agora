/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */
package com.nibodha.ip.launcher;

import com.nibodha.ip.camel.RouteDefinitionsInjector;
import com.nibodha.ip.config.PlatformConfiguration;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@Import(PlatformConfiguration.class)
public class PlatformLauncher extends SpringBootServletInitializer {


    public static void main(final String[] args) {
        new PlatformLauncher().run(args);

    }
åå
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

        return factory;
    }


}
