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

package com.nibodha.ip.services.re.components.rs;

import org.apache.camel.Component;
import org.apache.camel.component.cxf.jaxrs.BeanIdAware;
import org.apache.cxf.jaxrs.AbstractJAXRSFactoryBean;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;

/**
 * @author gibugeorge on 16/01/16.
 * @version 1.0
 */
public class RsSpringEndpoint extends RsEndpoint implements BeanIdAware {
    private AbstractJAXRSFactoryBean bean;
    private String beanId;


    public RsSpringEndpoint(final Component component, final String uri, final AbstractJAXRSFactoryBean bean) {
        super(uri, component);
        setAddress(bean.getAddress());
        // Update the sfb address by resolving the properties
        bean.setAddress(getAddress());
        init(bean);
    }

    private void init(final AbstractJAXRSFactoryBean bean) {
        this.bean = bean;
        if (bean instanceof BeanIdAware) {
            setBeanId(((BeanIdAware) bean).getBeanId());
        }
    }

    @Override
    protected JAXRSServerFactoryBean newJAXRSServerFactoryBean() {
        checkBeanType(bean, JAXRSServerFactoryBean.class);
        return (JAXRSServerFactoryBean) bean;
    }

    @Override
    protected JAXRSClientFactoryBean newJAXRSClientFactoryBean() {
        checkBeanType(bean, JAXRSClientFactoryBean.class);
        return (JAXRSClientFactoryBean) bean;
    }

    @Override
    public String getBeanId() {
        return beanId;
    }

    @Override
    public void setBeanId(final String id) {
        this.beanId = id;
    }
}
