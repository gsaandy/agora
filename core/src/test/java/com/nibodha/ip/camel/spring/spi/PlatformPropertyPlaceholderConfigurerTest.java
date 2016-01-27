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

package com.nibodha.ip.camel.spring.spi;

import com.nibodha.ip.services.config.PlatformPlaceHolderConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author gibugeorge on 18/01/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PlatformPlaceHolderConfiguration.class})
public class PlatformPropertyPlaceholderConfigurerTest {

    static {
        System.setProperty("config.location","classpath:");
    }
    //loaded from yaml
    @Value("${spring.application.name}")
    private String applicationName;

    //loaded from properties
    @Value("${test.property}")
    private String testProperty;

    @Test
    public void testPlatformPlaceholderConfigurer() {
        assertEquals("test", testProperty);
        assertEquals("Test", applicationName);
    }
}
