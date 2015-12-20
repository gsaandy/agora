/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
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
