package com.nibodha.ip.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class RouteDefinitionsInjector implements ApplicationContextAware, CamelContextAware {

    private ApplicationContext applicationContext;

    private CamelContext camelContext;

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

    public void addRoutes() {

    }

}
