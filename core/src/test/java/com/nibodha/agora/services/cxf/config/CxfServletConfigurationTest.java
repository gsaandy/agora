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

package com.nibodha.agora.services.cxf.config;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author gibugeorge on 01/03/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CxfServeletConfiguration.class})
public class CxfServletConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testCxfServletConfiguration() {
        final ServletRegistrationBean servletRegistrationBean = applicationContext.getBean(ServletRegistrationBean.class);
        Assert.assertNotNull(servletRegistrationBean);
        Assert.assertEquals("CXFServlet",servletRegistrationBean.getServletName());
        Assert.assertEquals("/services/*",servletRegistrationBean.getUrlMappings().iterator().next());
    }

}
