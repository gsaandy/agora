/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */
package com.nibodha.ip.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:META-INF/spring/nip-application-context.xml")
@EnableAutoConfiguration
@Import({ActuatorConfiguration.class, WebAppConfiguration.class})
public class PlatformConfiguration {
}
