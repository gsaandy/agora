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

import com.nibodha.ip.exceptions.PlatformRuntimeException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;

/**
 * @author gibugeorge on 17/02/16.
 * @version 1.0
 */
public class DatasourceConfiguration implements BeanDefinitionRegistryPostProcessor {


    private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceConfiguration.class);

    private final boolean isDataSourceEnabled;

    private final Environment environment;


    public DatasourceConfiguration(final Environment environment) {
        this.environment = environment;
        isDataSourceEnabled = Boolean.parseBoolean(environment.getProperty("platform.jdbc.datasource.enabled"));
    }

    public boolean dataSourceConfiguration(final BeanDefinitionRegistry beanDefinitionRegistry) {

        if (!isDataSourceEnabled && LOGGER.isInfoEnabled()) {
            LOGGER.info("Platform datasource is disabled");
            return false;
        }
        String[] dataSourceNames = getDsNames();
        if (isDataSourceEnabled) {
            for (String dataSourceName : dataSourceNames) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Configuring datasource with name {}", dataSourceName);
                }
                final HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setPoolName(dataSourceName);
                final String jdbcUrl = getProperty("jdbc-url", dataSourceName);
                if (StringUtils.isEmpty(jdbcUrl)) {
                    throw new PlatformRuntimeException(PlatformRuntimeException.Type.DATA_SOURCE_CONFIG, "JDBC Url must be configured");
                }
                hikariConfig.setJdbcUrl(jdbcUrl);
                final String userName = getProperty("user-name", dataSourceName) != null ? getProperty("user-name", dataSourceName) : "";
                hikariConfig.setUsername(userName);
                final String password = getProperty("password", dataSourceName) != null ? getProperty("password", dataSourceName) : "";
                hikariConfig.setPassword(password);
                hikariConfig.addDataSourceProperty("cachePrepStmts", getProperty("cache-prep-stmts", dataSourceName));
                hikariConfig.addDataSourceProperty("prepStmtCacheSize", getProperty("prep-stmt-cache-size", dataSourceName));
                hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", getProperty("prep-stmt-cache-sql-limit", dataSourceName));
                final GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
                genericBeanDefinition.setBeanClass(HikariDataSource.class);
                final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                constructorArgumentValues.addGenericArgumentValue(hikariConfig);
                genericBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
                final MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
                mutablePropertyValues.add("maximumPoolSize", getProperty("maximum-pool-size", dataSourceName));
                mutablePropertyValues.add("maxLifetime", getProperty("max-life-time", dataSourceName));
                mutablePropertyValues.add("idleTimeout", getProperty("idle-timeout", dataSourceName));
                genericBeanDefinition.setPropertyValues(mutablePropertyValues);
                beanDefinitionRegistry.registerBeanDefinition(dataSourceName, genericBeanDefinition);
            }

        }
        return true;
    }


    private String getProperty(final String key, final String dataSourceName) {
        final String actualKey = "platform.jdbc.datasource." + dataSourceName + "." + key;
        return environment.getProperty(actualKey) != null ? environment.getProperty(actualKey) : environment.getProperty("platform.jdbc.datasource.default." + key);
    }

    private String[] getDsNames() {
        final String dsNameProperty = environment.getProperty("platform.jdbc.datasource.names");
        final String[] dsNames;
        if (StringUtils.isNotEmpty(dsNameProperty)) {
            dsNames = dsNameProperty.split(",");
        } else {
            dsNames = new String[]{"defaultDS"};
        }
        return dsNames;
    }

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
        dataSourceConfiguration(registry);
    }
}
