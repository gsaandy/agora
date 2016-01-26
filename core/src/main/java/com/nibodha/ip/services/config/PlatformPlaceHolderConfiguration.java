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

package com.nibodha.ip.services.config;

import com.nibodha.ip.camel.spring.spi.PlatformPropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * @author gibugeorge on 20/01/16.
 * @version 1.0
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class PlatformPlaceHolderConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Bean
    public PlatformPropertyPlaceholderConfigurer platformPropertyPlaceholderConfigurer() throws IOException {

        final Resource configLocation = resourceLoader.getResource(System.getProperty("config.location"));
        final PlatformPropertyPlaceholderConfigurer platformPropertyPlaceholderConfigurer = new PlatformPropertyPlaceholderConfigurer();
        platformPropertyPlaceholderConfigurer.setConfigFileLocation(configLocation);
        platformPropertyPlaceholderConfigurer.setFileEncoding("UTF-8");
        platformPropertyPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        platformPropertyPlaceholderConfigurer.setFileNames("classpath:application.properties, *.yaml,*.properties");
        return platformPropertyPlaceholderConfigurer;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
