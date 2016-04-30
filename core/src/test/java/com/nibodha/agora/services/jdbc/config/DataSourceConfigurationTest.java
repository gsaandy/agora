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

package com.nibodha.agora.services.jdbc.config;

import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

/**
 * @author gibugeorge on 18/02/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/datasource-config-test-context.xml"})
public class DataSourceConfigurationTest {

    private static Server server;

    @Autowired
    private ApplicationContext applicationContext;


    @BeforeClass
    public static void setup() throws SQLException {
        System.setProperty("config.location","classpath:");
        server = Server.createTcpServer().start();
    }

    @Test
    public void testDataSourceConfiguration(){
        final HikariDataSource testDS = applicationContext.getBean("testDS", HikariDataSource.class);
        Assert.assertNotNull(testDS);
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }
}
