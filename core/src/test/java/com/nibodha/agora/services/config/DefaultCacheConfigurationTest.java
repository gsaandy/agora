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

package com.nibodha.agora.services.config;

import com.nibodha.agora.services.cache.config.CacheConfiguration;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author gibugeorge on 27/01/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {PropertyPlaceholderAutoConfiguration.class, CacheConfiguration.class})
public class DefaultCacheConfigurationTest {


    @Autowired
    private EmbeddedCacheManager embeddedCacheManager;

    @Before
    public void setUp() {
        embeddedCacheManager.getCache("platformCache").put("test1","test1");
        embeddedCacheManager.getCache("platformCache").put("test2","test2");
    }

    @Test
    public void testGetFromCache() {
        final Cache cache = embeddedCacheManager.getCache("platformCache");
        Assert.assertEquals("test1",cache.get("test1"));
    }

    @Test
    public void testPutInCache() {
        final Cache cache = embeddedCacheManager.getCache("platformCache");
        cache.put("test", "test");
        Assert.assertEquals("test",cache.get("test"));
    }

    @Test
    public void testRemoveFromCache() {
        final Cache cache = embeddedCacheManager.getCache("platformCache");
        cache.remove("test1");
        Assert.assertNull(cache.get("test1"));
    }

    @Test
    public void testUpdateCache() {
        final Cache cache = embeddedCacheManager.getCache("platformCache");
        Assert.assertEquals("test2",cache.get("test2"));
        cache.put("test2", "test");
        Assert.assertEquals("test",cache.get("test2"));
    }



}
