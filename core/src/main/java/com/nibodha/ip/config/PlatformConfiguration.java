package com.nibodha.ip.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:META-INF/spring/nip-application-context.xml")
public class PlatformConfiguration {
}
