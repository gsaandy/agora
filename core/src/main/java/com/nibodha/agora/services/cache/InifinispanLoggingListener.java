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

package com.nibodha.agora.services.cache;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gibugeorge on 24/01/16.
 * @version 1.0
 */
@Listener
public class InifinispanLoggingListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(InifinispanLoggingListener.class);

    @CacheEntryCreated
    public void observeAdd(final CacheEntryCreatedEvent<String, String> event) {
        if (event.isPre())
            return;
        LOGGER.info("Cache entry {} = {} added in cache {}", event.getKey(), event.getValue(), event.getCache().getName());
    }

    @CacheEntryModified
    public void observeUpdate(final CacheEntryModifiedEvent<String, String> event) {
        if (event.isPre())
            return;
        LOGGER.info("Cache entry {} = {} modified in cache %s", event.getKey(), event.getValue(), event.getCache().getName());
    }

    @CacheEntryRemoved
    public void observeRemove(final CacheEntryRemovedEvent<String, String> event) {
        if (event.isPre())
            return;
        LOGGER.info("Cache entry {} removed in cache {}", event.getKey(), event.getCache().getName());
    }
}
