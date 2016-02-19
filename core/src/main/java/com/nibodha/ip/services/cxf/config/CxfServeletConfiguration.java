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

package com.nibodha.ip.services.cxf.config;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author gibugeorge on 19/02/16.
 * @version 1.0
 */
@Configuration
@ImportResource({"classpath:META-INF/cxf/cxf.xml", "classpath:META-INF/cxf/cxf-servlet.xml"})
public class CxfServeletConfiguration {


    @Bean
    public ServletRegistrationBean cxfServlet() {
        final CXFServlet cxfServlet = new CXFServlet();
        final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(cxfServlet, "/services/*");
        servletRegistrationBean.setAsyncSupported(true);
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.setName("CXFServlet");
        return servletRegistrationBean;
    }


}
