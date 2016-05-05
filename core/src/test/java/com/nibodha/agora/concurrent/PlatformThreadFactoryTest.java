/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.concurrent;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author gibugeorge on 03/05/16.
 * @version 1.0
 */
public class PlatformThreadFactoryTest {

    @Test
    public void testThreadFactory() {
        final PlatformThreadFactory platformThreadFactory = new PlatformThreadFactory("prefix");
        final Thread thread = platformThreadFactory.newThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        Assert.assertEquals("prefix-1-thread-1", thread.getName());

    }
}
