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

package com.nibodha.ip.services.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author gibugeorge on 25/02/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "platform.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(DigestAuthenticationConfiguration.class)
public class PlatformSecurityConfiguration {

    private static final String DEFAULT_FILTER_NAME = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;


    @Bean
    public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration() {
        DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean(
                DEFAULT_FILTER_NAME);
        registration.setOrder(SecurityProperties.BASIC_AUTH_ORDER);
        return registration;
    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher(
            ApplicationEventPublisher publisher) {
        return new DefaultAuthenticationEventPublisher(publisher);
    }

}
