/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */
package com.nibodha.ip.camel;

import com.nibodha.ip.exceptions.PlatformRuntimeException;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.CamelRouteContextFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RouteDefinitionsInjector implements ApplicationContextAware, CamelContextAware {

    private ApplicationContext applicationContext;

    private CamelContext camelContext;

    private final Logger LOGGER = LoggerFactory.getLogger(RouteDefinitionsInjector.class);

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setCamelContext(final CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return this.camelContext;
    }

    public void inject() {
        addRoutes();
    }

    private void addRoutes() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Getting route contexts from spring application contexts");
        }
        final Map<String, CamelRouteContextFactoryBean> routeContexts = applicationContext.getBeansOfType(CamelRouteContextFactoryBean.class);
        final Set<String> routeContextNames = routeContexts.keySet();
        for (final String routeContextName : routeContextNames) {
            if (StringUtils.isEmpty(routeContextName)) {
                throw new PlatformRuntimeException("The route context id cannot be null");

            }
            final String routeContextId = routeContextName.replace("&", "");
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Getting routes from route context:: " + routeContextId);
            }
            final List<RouteDefinition> routeDefinitions = routeContexts.get(routeContextName).getRoutes();
            for (final RouteDefinition routeDefinition : routeDefinitions) {
                final String routeDefinitionId = routeDefinition.getId();
                if (isAlreadyDefined(routeDefinition)) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Route " + routeDefinitionId + " is already added to the camel context");
                    }
                    continue;
                }
                if (StringUtils.isEmpty(routeDefinitionId)) {
                    throw new PlatformRuntimeException("The route id cannot be null");
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Adding route definition with id " + routeDefinitionId + " from route context with id " + routeContextId + " to camel context");
                }
                routeDefinition.setGroup(routeContextId);
                try {
                    this.camelContext.addRouteDefinition(routeDefinition);
                } catch (Exception e) {
                    LOGGER.error("Exception occured while adding route definition with id " + routeDefinition + " to camel context", e);
                }
            }
        }

    }

    private boolean isAlreadyDefined(final RouteDefinition route) {
        return camelContext.getRouteDefinition(route.getId()) != null;
    }

}
