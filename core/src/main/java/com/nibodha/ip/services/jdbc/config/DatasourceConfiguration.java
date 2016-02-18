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

package com.nibodha.ip.services.jdbc.config;

import com.nibodha.ip.services.jdbc.DataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 17/02/16.
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DatasourceConfiguration {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean(name = "plartformDataSource")
    @ConditionalOnProperty(prefix = "platform.jdbc.datasource", value = "enabled", havingValue = "true", matchIfMissing = false)
    public HikariDataSource hikariDataSource() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataSourceProperties.getJdbcUrl());
        hikariConfig.setUsername(dataSourceProperties.getUserName());
        hikariConfig.setPassword(dataSourceProperties.getPasssword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", dataSourceProperties.isCachePrepStmts());
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", dataSourceProperties.getPrepStmtCacheSize());
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", dataSourceProperties.getPrepStmtCacheSqlLimit());
        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        hikariDataSource.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        hikariDataSource.setMaxLifetime(dataSourceProperties.getMaxLifeTime());
        hikariDataSource.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        return hikariDataSource;
    }
}
