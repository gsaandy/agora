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

package com.nibodha.ip.services.file;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author gibugeorge on 21/03/16.
 * @version 1.0
 */
public class FileWatcher extends AbstractDirectoryWatcher {

    private final Resource fileTobeWatched;
    private final FileWatcherCallback fileWatcherCallback;

    public FileWatcher(final Resource fileTobeWatched, final FileWatcherCallback fileWatcherCallback) throws IOException {
        super(fileTobeWatched.getFile().getParentFile().toURI());
        this.fileTobeWatched = fileTobeWatched;
        this.fileWatcherCallback = fileWatcherCallback;
    }

    @Override
    public void entryModified(WatchEvent event) throws IOException {
        final WatchEvent<Path> ev = (WatchEvent<Path>) event;
        final String fileName = ev.context().toFile().getName();
        if (fileName.equals(fileTobeWatched.getFile().getName())) {
            this.fileWatcherCallback.execute();
        }
    }
}
