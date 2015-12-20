/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.ip.camel.spring.spi;

import com.nibodha.ip.yaml.YamlPropertiesLoader;
import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author gibugeorge on 17/12/15.
 * @version 1.0
 */
public class PlatformPropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {


    private Resource configFileLocation;
    private final ResourcePatternResolver resourcePatternResolver;
    private String fileEncoding;
    private Resource[] locations;
    private Boolean ignoreResourceNotFound;
    private YamlPropertiesLoader yamlPropertiesLoader;

    public PlatformPropertyPlaceholderConfigurer() {
        resourcePatternResolver = new PathMatchingResourcePatternResolver();
        yamlPropertiesLoader = new YamlPropertiesLoader();
    }

    public void setConfigFileLocation(final Resource configFileLocation) throws IOException {
        this.configFileLocation = configFileLocation;

    }

    public void setFileNames(final String fileNames) throws IOException {
        final String[] fileNamesArray = fileNames.split(",");
        final List<Resource> locations = new ArrayList<Resource>();
        for (final String fileName : fileNamesArray) {
            final String absoluteFilePathPattern = configFileLocation.getURL().toString() + File.separator + fileName;
            final Resource[] resources = resourcePatternResolver.getResources(absoluteFilePathPattern);
            locations.addAll(Arrays.asList(resources));
        }
        final Resource[] resources = new Resource[locations.size()];
        this.locations = locations.toArray(resources);
        super.setLocations(this.locations);
    }

    protected void loadProperties(Properties props) throws IOException {
        if (this.locations != null) {
            for (final Resource location : this.locations) {
                if (logger.isInfoEnabled()) {
                    logger.info("Loading properties file from " + location);
                }
                try {
                    if (location.getFilename().endsWith(".yaml")) {
                        yamlPropertiesLoader.setResources(location);
                        props.putAll(yamlPropertiesLoader.createProperties());
                    } else {
                        PropertiesLoaderUtils.fillProperties(
                                props, new EncodedResource(location, this.fileEncoding));
                    }
                } catch (IOException ex) {
                    if (this.ignoreResourceNotFound) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
                        }
                    } else {
                        throw ex;
                    }
                }
            }
        }
    }

    public void setFileEncoding(String encoding) {
        super.setFileEncoding(encoding);
        this.fileEncoding = encoding;
    }

    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        super.setIgnoreResourceNotFound(ignoreResourceNotFound);
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }


}
