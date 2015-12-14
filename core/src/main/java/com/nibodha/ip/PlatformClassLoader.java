/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.ip;

import javassist.ClassPool;
import javassist.Loader;

/**
 * @author Gibu George (gibu.george@nibodha.com)
 * @version 1.0
 */
public class PlatformClassLoader extends Loader {

    public PlatformClassLoader(ClassLoader parent) {
        super(parent, ClassPool.getDefault());

    }
}
