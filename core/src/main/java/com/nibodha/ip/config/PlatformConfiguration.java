package com.nibodha.ip.config;

import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
//@Import({EmbeddedServletContainerAutoConfiguration.class, ActuatorConfiguration.class,
//        WebMvcAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/nip-application-context.xml")
public class PlatformConfiguration {
}
