/*
 * Copyright 2016 Nibodha Technologies Pvt. Ltd.
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

package com.nibodha.agora.services.config;

import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.jmx.ConnectorServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.management.remote.JMXServiceURL;
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

    @Inject
    private ResourceLoader resourceLoader;

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
        return new JettyEmbeddedServletContainerFactory();
    }

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            @Value("${server.port:8080}") final String mainPort, @Value("${jetty.config.path:}") final String jettyConfigXmlPath,
            @Value("${platform.jmx.port:1099}") final String jmxPort) {
        final JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addServerCustomizers(new EmbeddedServerCustomizer(jettyConfigXmlPath, jmxPort, Integer.valueOf(mainPort)));
        return factory;
    }

    private class EmbeddedServerCustomizer implements JettyServerCustomizer {

        private final String jettyConfigXmlPath;
        private final String jmxPort;
        private final Integer port;

        public EmbeddedServerCustomizer(final String jettyConfigXmlPath, String jmxPort, Integer port) {
            this.jettyConfigXmlPath = jettyConfigXmlPath;
            this.jmxPort = jmxPort;
            this.port = port;
        }

        @Override
        public void customize(final Server server) {
            //Remove the default http 1.1 connector and add both http1.1 and http2 connectors
            server.removeConnector(server.getConnectors()[0]);
            final HttpConfiguration httpConfiguration = new HttpConfiguration();
            final HttpConnectionFactory http1 = new HttpConnectionFactory(httpConfiguration);
            final HTTP2CServerConnectionFactory http2 = new HTTP2CServerConnectionFactory(httpConfiguration);
            final ServerConnector serverConnector = new ServerConnector(server, http1, http2);
            serverConnector.setPort(port);
            server.addConnector(serverConnector);
            if (!StringUtils.isEmpty(jettyConfigXmlPath)) {
                try {
                    final XmlConfiguration xmlConfiguration = new XmlConfiguration(new FileInputStream(jettyConfigXmlPath));
                    xmlConfiguration.configure(server);

                } catch (SAXException | IOException e) {
                    LOGGER.error("Exception reading the xml configuration", e);
                } catch (Exception e) {
                    LOGGER.error("Exception configuring the server configuration", e);
                }
            }
            try {
                final ConnectorServer connectorServer = new ConnectorServer(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + jmxPort + "/jmxrmi"), "com.nibodha.ip.jmx:name=rmiconnectorserver");
                server.addBean(connectorServer);
            } catch (Exception e) {
                LOGGER.error("Exception configuring the mbean connector server", e);
            }

            // Expose Jetty managed beans to the JMX platform server provided by Spring
            final MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
            mbContainer.setDomain("com.nibodha.ip");
            server.addBean(mbContainer);


        }
    }
}
