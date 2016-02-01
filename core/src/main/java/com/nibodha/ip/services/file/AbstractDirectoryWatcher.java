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

package com.nibodha.ip.services.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author gibugeorge on 28/01/16.
 * @version 1.0
 */
public abstract class AbstractDirectoryWatcher implements Runnable, DirectoryWatcher{


    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractDirectoryWatcher.class);
    protected static final long DEFAULT_CHANGES_CHECK_INTERVAL_MS = 1000;

    private final WatchService directoryWatcher;
    private final URI directory;

    public AbstractDirectoryWatcher(final URI directory) throws IOException {
        directoryWatcher = FileSystems.getDefault().newWatchService();
        this.directory = directory;
        final Path configDir = Paths.get(directory);
        configDir.register(directoryWatcher, ENTRY_CREATE, ENTRY_MODIFY);

    }

    @Override
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Checking for changes in directory {}", directory);
        }
        WatchKey key;
        try {
            // wait for a key to be available
            key = directoryWatcher.take();
        } catch (InterruptedException ex) {
            return;
        }
        for (final WatchEvent<?> event : key.pollEvents()) {
            final WatchEvent.Kind<?> kind = event.kind();
            if (LOGGER.isInfoEnabled()) {
                final WatchEvent<Path> ev = (WatchEvent<Path>) event;
                final Path fileName = ev.context();
                LOGGER.info(kind.name() + " : " + fileName);
            }

            try {
                if (kind == ENTRY_MODIFY) {
                    entryModified();
                }
            } catch (IOException e) {
                LOGGER.error("Exception while processing watched directory {}", directory, e);
            }
            boolean valid = key.reset();
            if (!valid) {
                break;
            }

        }

    }

}
