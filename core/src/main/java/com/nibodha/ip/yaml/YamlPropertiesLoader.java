/*
 * Copyright 2016 Nibodha Trechnologies Pvt. Ltd.
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

package com.nibodha.ip.yaml;

import org.springframework.beans.factory.config.YamlProcessor;

import java.util.Map;
import java.util.Properties;

/**
 * @author gibugeorge on 18/12/15.
 * @version 1.0
 */
public class YamlPropertiesLoader extends YamlProcessor{
    public Properties createProperties() {
        final Properties result = new Properties();
        process(new MatchCallback() {
            @Override
            public void process(Properties properties, Map<String, Object> map) {
                result.putAll(properties);
            }
        });
        return result;
    }
}
