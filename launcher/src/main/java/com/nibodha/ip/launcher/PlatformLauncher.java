/**
 *
 */
package com.nibodha.ip.launcher;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableAdminServer
public class PlatformLauncher {

    public static void main(final String[] args) {
        final SpringApplication application = new SpringApplication(PlatformLauncher.class);
        application.setRegisterShutdownHook(true);
        application.setWebEnvironment(true);
        application.setLogStartupInfo(true);
        application.run(args);
    }



}
