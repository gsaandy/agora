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

package com.nibodha.ip.services.cache.config;

import com.nibodha.ip.services.cache.InifinispanLoggingListener;
import com.nibodha.ip.services.mq.config.PlatformMqConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.Cache;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * @author gibugeorge on 24/01/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "platform.cache", value = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(PlatformMqConfiguration.class)
public class CacheConfiguration {

    public static final String DEFAULT_PLATFORM_CACHE_NAME = "platformCache";

    private static Logger LOGGER = LoggerFactory.getLogger(CacheConfiguration.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public EmbeddedCacheManager infinispanCacheManager(@Value("${platform.cache.config.path:}") final String cacheConfigPath) throws IOException {
        final EmbeddedCacheManager cacheManager = createEmbeddedCacheManager(cacheConfigPath);
        if (StringUtils.isEmpty(cacheConfigPath)) {
            cacheManager.getCache(DEFAULT_PLATFORM_CACHE_NAME, true);
        }
        final Set<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            final Cache cache = cacheManager.getCache(cacheName);
            cache.start();
            cache.addListener(new InifinispanLoggingListener());
        }
        return cacheManager;
    }

    private EmbeddedCacheManager createEmbeddedCacheManager(final String cacheConfigPath) throws IOException {
        if (StringUtils.isNotEmpty(cacheConfigPath)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Configuring cache based on configuration file " + cacheConfigPath);
            }
            InputStream in = resourceLoader.getResource(cacheConfigPath).getInputStream();
            try {
                return new DefaultCacheManager(in);
            } finally {
                in.close();
            }
        }
        return new DefaultCacheManager(getDefaultCacheConfiguration(), true);
    }

    private GlobalConfiguration getDefaultCacheConfiguration() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Configuring default p[latform cache");
        }
        return GlobalConfigurationBuilder
                .defaultClusteredBuilder()
                .nonClusteredDefault()
                .globalJmxStatistics()
                .cacheManagerName("Platform Cache Manager").jmxDomain("com.nibodha.ip.cache")
                .enable()
                .build();

    }
}
