package com.nibodha.ip.launcher.config;

import com.nibodha.ip.core.config.batch.BatchAdminServeletConfiguration;
import com.nibodha.ip.core.config.batch.BatchWebAppConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by gibugeorge on 01/12/15.
 */
@Configuration
@Import({BatchAdminServeletConfiguration.class, BatchWebAppConfiguration.class})
public class PlatformConfiguration {
}
