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

package com.nibiodha.ip.test.jpa;

import dk.tigerteam.trimm.mdsd.runtime.RuntimeMetaClazz;
import dk.tigerteam.trimm.mdsd.runtime.RuntimeMetaProperty;
import dk.tigerteam.trimm.persistence.hibernate.util.HibernateProxyHandler;
import dk.tigerteam.trimm.persistence.mdsd.test.AbstractModelTest;
import dk.tigerteam.trimm.persistence.mdsd.test.RuntimeMetaTestDataCreator;
import dk.tigerteam.trimm.persistence.util.ObjectPrinter;
import dk.tigerteam.trimm.persistence.util.ProxyHandler;
import dk.tigerteam.trimm.util.IndentPrintWriter;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author gibugeorge on 08/02/16.
 * @version 1.0
 */
public class BaseModelTest extends AbstractModelTest{

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("testModel");
    }

    @AfterClass
    public static void teardown() {
        emf.close();
    }

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public ProxyHandler getProxyHandler(EntityManager entityManager) {
        return new HibernateProxyHandler(entityManager);
    }

    @Override
    public Field[] getIgnoreFieldsDuringGraphCompare(Class<?> aClass) {
        return new Field[0];
    }

    // Support for pretty printing tests errors that contain LocalDate
    @Override
    public ObjectPrinter[] getObjectPrinters() {
        return new ObjectPrinter[]{new ObjectPrinter() {
            @Override
            public boolean support(Object object) {
                return object instanceof LocalDate;
            }

            @Override
            public void print(Object object, Set<Object> printedObjects, IndentPrintWriter printWriter) {
                printWriter.print(((LocalDate) object).toString());
            }
        }};
    }

    // Support for LocalDate in test data
    @Override
    protected RuntimeMetaTestDataCreator lazyCreateDataCreator() {
        return new RuntimeMetaTestDataCreator() {
            @Override
            protected Object newInstance(RuntimeMetaClazz metaClazz,
                                         ObjectInstancesStack objectInstancesStack,
                                         RuntimeMetaProperty metaProperty) {
                Class<?> _class = metaClazz.getJavaClass();
                if (_class != null && LocalDate.class.equals(_class)) {
                    return new LocalDate();
                } else {
                    return super.newInstance(metaClazz, objectInstancesStack, metaProperty);
                }
            }
        };
    }
}
