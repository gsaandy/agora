/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */
package com.nibodha.ip.launcher;

import com.nibodha.ip.camel.RouteDefinitionsInjector;
import com.nibodha.ip.config.PlatformConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@Import(PlatformConfiguration.class)
public class PlatformLauncher extends SpringBootServletInitializer {


    public static void main(final String[] args) {
        new PlatformLauncher().run(args);

    }

    public void run(final String[] args) {
        //Thread.currentThread().setContextClassLoader(new PlatformClassLoader(Thread.currentThread().getContextClassLoader()));
        final SpringApplication application = new SpringApplication(PlatformLauncher.class);
        application.setRegisterShutdownHook(true);
        application.setWebEnvironment(true);
        application.setLogStartupInfo(true);
        final ConfigurableApplicationContext configurableApplicationContext = application.run(args);
        final RouteDefinitionsInjector routeDefinitionsInjector = configurableApplicationContext.getBean(RouteDefinitionsInjector.class);
        routeDefinitionsInjector.inject();

    }


}
