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

package com.nibodha.ip.services.re.spring;

import com.nibodha.ip.services.re.spring.spi.PlatformPropertyPlaceholderConfigurer;
import com.nibodha.ip.services.file.AbstractDirectoryWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.WatchEvent;

/**
 * @author gibugeorge on 28/01/16.
 * @version 1.0
 */
@Component
public class ConfigurationDirectoryWatcher extends AbstractDirectoryWatcher {

    private final PlatformPropertyPlaceholderConfigurer platformPropertyPlaceholderConfigurer;


    @Autowired
    public ConfigurationDirectoryWatcher(final PlatformPropertyPlaceholderConfigurer platformPropertyPlaceholderConfigurer, final ResourceLoader resourceLoader) throws IOException, URISyntaxException {
        super(resourceLoader.getResource(System.getProperty("config.location")).getURI());
        this.platformPropertyPlaceholderConfigurer = platformPropertyPlaceholderConfigurer;
    }

    @Override
    public void entryModified(final WatchEvent event) throws IOException {
        this.platformPropertyPlaceholderConfigurer.refresh();
    }
}
