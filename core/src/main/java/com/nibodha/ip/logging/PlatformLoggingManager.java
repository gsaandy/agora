/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.ip.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gibugeorge on 21/12/15.
 * @version 1.0
 */
public class PlatformLoggingManager {

    private static final Logger logger = LoggerFactory.getLogger(PlatformLoggingManager.class);

    private final String configPath;

    public PlatformLoggingManager(String configPath) {
        this.configPath = configPath;
    }

    public void configure() {
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator configurator = new JoranConfigurator();
        try {
            configurator.setContext(context);
            configurator.doConfigure(configPath);

        } catch (JoranException e) {
            logger.error("Exception configuring logback using the provided config file " + configPath, e);
        }
    }
}
