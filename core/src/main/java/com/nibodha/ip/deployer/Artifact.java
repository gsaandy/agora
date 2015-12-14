/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */
package com.nibodha.ip.deployer;

import org.springframework.boot.loader.archive.ExplodedArchive;

import java.io.File;

/**
 * @author Gibu George (gibu.george@nibodha.com)
 * @version 1.0
 */
public class Artifact extends ExplodedArchive{

    public Artifact(File root, boolean recursive) {
        super(root, recursive);
    }
}
