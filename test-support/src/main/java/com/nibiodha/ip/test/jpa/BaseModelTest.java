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

package com.nibiodha.ip.test.jpa;

import dk.tigerteam.trimm.persistence.mdsd.test.AbstractModelTest;
import dk.tigerteam.trimm.persistence.util.ObjectPrinter;
import dk.tigerteam.trimm.persistence.util.ProxyHandler;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;

/**
 * @author gibugeorge on 08/02/16.
 * @version 1.0
 */
public class BaseModelTest extends AbstractModelTest{
    @Override
    public ProxyHandler getProxyHandler(EntityManager entityManager) {
        return null;
    }

    @Override
    public Field[] getIgnoreFieldsDuringGraphCompare(Class<?> classBeingCompared) {
        return new Field[0];
    }

    @Override
    public ObjectPrinter[] getObjectPrinters() {
        return new ObjectPrinter[0];
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }
}
