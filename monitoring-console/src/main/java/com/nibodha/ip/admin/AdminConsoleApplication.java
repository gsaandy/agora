package com.nibodha.ip.admin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by gibugeorge on 01/12/15.
 */
@SpringBootApplication
@EnableAdminServer
public class AdminConsoleApplication {

    public static void main(final String[] args) {
        final SpringApplication application = new SpringApplication(AdminConsoleApplication.class);
        application.setRegisterShutdownHook(true);
        application.setWebEnvironment(true);
        application.setLogStartupInfo(true);
        application.run(args);
    }
}
