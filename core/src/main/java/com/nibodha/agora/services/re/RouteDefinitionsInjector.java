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

package com.nibodha.agora.services.re;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.CamelRouteContextFactoryBean;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RouteDefinitionsInjector implements ApplicationContextAware, CamelContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private CamelContext camelContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteDefinitionsInjector.class);

    @PropertyInject("camel.route.id.patterns")
    private String routeIdPatternsToInject;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext){
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

    protected void inject(){
        addSpringDslRoutes();
        addJavaDslRoutes();
    }

    private void addSpringDslRoutes() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Getting route contexts from spring application contexts");
        }
        final Map<String, CamelRouteContextFactoryBean> routeContexts = applicationContext.getBeansOfType(CamelRouteContextFactoryBean.class);
        if (MapUtils.isEmpty(routeContexts)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("No route contexts found");
                return;
            }
        }

        final Set<String> routeContextNames = routeContexts.keySet();
        for (final String routeContextName : routeContextNames) {
            if (StringUtils.isEmpty(routeContextName)) {
                if(LOGGER.isInfoEnabled()) {
                    LOGGER.info("Skipping the route context as id is null");
                }
                continue;

            }
            final String routeContextId = routeContextName.replace("&", "");
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Getting routes from route context:: " + routeContextId);
            }
            final List<RouteDefinition> routeDefinitions = routeContexts.get(routeContextName).getRoutes();
            addRoutesToCamelContext(routeContextId, routeDefinitions);
        }

    }

    private void addRoutesToCamelContext(String routeContextId, List<RouteDefinition> routeDefinitions) {
        for (final RouteDefinition routeDefinition : routeDefinitions) {
            final String routeDefinitionId = routeDefinition.getId();
            if (StringUtils.isEmpty(routeDefinitionId)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Rejecting route as routedefinition id is null");
                }
                continue;
            }
            if(!routeDefinitionId.startsWith(routeIdPatternsToInject)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Rejecting route " + routeDefinitionId + "doesn't match the pattern "+ routeIdPatternsToInject);
                }
                continue;
            }
            if (isAlreadyDefined(routeDefinition)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Route " + routeDefinitionId + " is already added to the camel context");
                }
                continue;
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

    private void addJavaDslRoutes() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Getting route builders from spring application contexts");
        }
        final Map<String, RouteBuilder> routeBuilders = applicationContext.getBeansOfType(RouteBuilder.class);
        if (MapUtils.isEmpty(routeBuilders)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("No route builders found");
            }
            return;
        }

        final Set<String> routeBuilderNames = routeBuilders.keySet();
        for (final String routeBuilderName : routeBuilderNames) {
            final RouteBuilder routeBuilder = routeBuilders.get(routeBuilderName);
            try {
                camelContext.addRoutes(routeBuilder);
            } catch (Exception e) {
                LOGGER.error("Exception while adding route builder "+routeBuilderName+" to context",e);
            }


        }

    }

    private boolean isAlreadyDefined(final RouteDefinition route) {
        return camelContext.getRouteDefinition(route.getId()) != null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inject();
    }
}
