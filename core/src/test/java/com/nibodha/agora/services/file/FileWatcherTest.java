/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.file;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @author gibugeorge on 03/05/16.
 * @version 1.0
 */
public class FileWatcherTest {

    private final Resource resource = new ClassPathResource("file-watch-test.properties");

    @Test
    public void testFileWatcher() throws Exception {
        final FileWatcherCallback fileWatcherCallback = Mockito.mock(FileWatcherCallback.class);
        final FileWatcher fileWatcher = new FileWatcher(resource, fileWatcherCallback);
        fileWatcher.start();
        final Properties properties = new Properties();
        properties.load(resource.getInputStream());
        properties.put("test1", "test1");
        properties.store(new FileOutputStream(resource.getFile()), "");
        Thread.sleep(10000);
        Mockito.verify(fileWatcherCallback).execute();
    }


}


