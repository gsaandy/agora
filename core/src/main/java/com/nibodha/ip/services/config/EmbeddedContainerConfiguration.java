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

package com.nibodha.ip.services.config;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * @author gibugeorge on 12/01/16.
 * @version 1.0
 */
@Configuration
public class EmbeddedContainerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformConfiguration.class);

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            @Value("${server.port:8080}") final String mainPort, @Value("${jetty.config.path:}") final String jettyConfigXmlPath) {
        final JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory(Integer.valueOf(mainPort));
        factory.addServerCustomizers(new JettyServerCustomizer() {
            @Override
            public void customize(final Server server) {
                // Expose Jetty managed beans to the JMX platform server provided by Spring
                if (!StringUtils.isEmpty(jettyConfigXmlPath)) {
                    try {
                        final XmlConfiguration xmlConfiguration = new XmlConfiguration(new FileInputStream(jettyConfigXmlPath));
                        xmlConfiguration.configure(server);
                    } catch (SAXException e) {
                        LOGGER.error("Exception reading the xml configuration", e);
                    } catch (IOException e) {
                        LOGGER.error("Exception reading the xml configuration", e);
                    } catch (Exception e) {
                        LOGGER.error("Exception configuring the server configuration", e);
                    }
                }
                final MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
                server.addBean(mbContainer);
            }
        });
        return factory;
    }
}
