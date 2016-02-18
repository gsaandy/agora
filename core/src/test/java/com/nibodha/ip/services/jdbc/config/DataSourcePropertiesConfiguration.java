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

import com.nibodha.ip.services.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 18/02/16.
 * @version 1.0
 */
@Configuration
public class DataSourcePropertiesConfiguration {


    @Bean
    public DataSourceProperties dataSourceProperties() {
        final DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setEnabled(true);
        dataSourceProperties.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        return dataSourceProperties;
    }
}
