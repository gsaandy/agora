/**
 *
 */
package com.nibodha.ip.launcher;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by gibugeorge on 28/11/15.
 */
@SpringBootApplication
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
