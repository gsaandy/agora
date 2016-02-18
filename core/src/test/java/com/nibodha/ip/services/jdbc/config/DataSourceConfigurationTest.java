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

import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.SQLException;

/**
 * @author gibugeorge on 18/02/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {DataSourcePropertiesConfiguration.class, DatasourceConfiguration.class})
public class DataSourceConfigurationTest {

    private static Server server;

    @Autowired
    private HikariDataSource hikariDataSource;

    @BeforeClass
    public static void setup() throws SQLException {
        System.setProperty("platform.jdbc.datasource.enabled","true");
        server = Server.createTcpServer().start();
    }

    @Test
    public void test(){
        Assert.assertNotNull(hikariDataSource);
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }
}
