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

package com.nibodha.agora.env;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author gibugeorge on 01/03/16.
 * @version 1.0
 */
public class PlatformEnvironment extends StandardEnvironment {

    private static final String ENV_CONFIG_FILE_EXTENSION = "*.properties";

    @Override
    protected void customizePropertySources(final MutablePropertySources propertySources) {
        super.customizePropertySources(propertySources);
        final String configLocString = System.getProperty("config.location");
        if (StringUtils.isNotEmpty(configLocString)) {
            try {
                final Properties properties = new Properties();
                final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
                final Resource[] resources;
                if (configLocString.startsWith("classpath:")) {
                    resources = resourcePatternResolver.getResources(ENV_CONFIG_FILE_EXTENSION);
                } else {
                    resources = resourcePatternResolver.getResources(configLocString + File.separator + ENV_CONFIG_FILE_EXTENSION);
                }

                for (final Resource resource : resources) {
                    properties.load(resource.getInputStream());
                }
                propertySources.addLast(new PropertiesPropertySource("externalizedProperties", properties));

            } catch (IOException e) {
                logger.error("Exception reading resources", e);
            }

        }
    }

}
