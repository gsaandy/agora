package com.nibodha.ip.core.config.batch;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by gibugeorge on 01/12/15.
 */

@Configuration
@ImportResource("classpath:/org/springframework/batch/admin/web/resources/servlet-config.xml")
public class BatchAdminServeletConfiguration {
}
