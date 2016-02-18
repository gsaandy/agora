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

import com.nibodha.ip.services.cache.CacheProperties;
import com.nibodha.ip.services.cache.InifinispanLoggingListener;
import com.nibodha.ip.services.mq.config.PlatformMqConfiguration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author gibugeorge on 24/01/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "platform.cache", value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CacheProperties.class)
@AutoConfigureAfter(PlatformMqConfiguration.class)
public class CacheConfiguration {

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public EmbeddedCacheManager infinispanCacheManager() throws IOException {
        final EmbeddedCacheManager cacheManager = createEmbeddedCacheManager();
        if (this.cacheProperties.getConfig() == null) {
            if (cacheProperties.getCacheNames() == null) {
                cacheManager.startCaches(CacheProperties.DEFAULT_PLATFORM_CACHE_NAME);
                cacheManager.getCache(CacheProperties.DEFAULT_PLATFORM_CACHE_NAME, true).addListener(new InifinispanLoggingListener());
            } else {
                cacheManager.startCaches(cacheProperties.getCacheNames());
                for (String cacheName : cacheProperties.getCacheNames()) {
                    cacheManager.getCache(cacheName, true).addListener(new InifinispanLoggingListener());
                }
            }
        }
        return cacheManager;
    }

    private EmbeddedCacheManager createEmbeddedCacheManager() throws IOException {
        Resource location = this.cacheProperties.getConfig();
        if (location != null) {
            InputStream in = location.getInputStream();
            try {
                return new DefaultCacheManager(in);
            } finally {
                in.close();
            }
        }
        return new DefaultCacheManager(getDefaultCacheConfiguration(), true);
    }

    private GlobalConfiguration getDefaultCacheConfiguration() {
        return GlobalConfigurationBuilder
                .defaultClusteredBuilder()
                .nonClusteredDefault()
                .globalJmxStatistics()
                .cacheManagerName("Platform Cache Manager")
                .enable()
                .build();

    }
}
