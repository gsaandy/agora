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

package com.nibodha.ip.dao;

import com.nibodha.ip.dao.config.JpaConfiguration;
import com.nibodha.ip.services.config.PlatformPlaceHolderConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {PlatformPlaceHolderConfiguration.class, JpaConfiguration.class})
public class SampleDaoTest {

    @Inject
    private SampleDao sampleDao;

    @BeforeClass
    public static void setup() throws SQLException {
        System.setProperty("config.location", "classpath:");
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback
    public void testCreate() {
        final SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId("id");
        sampleEntity.setName("name");
        sampleEntity.setValue("value");
        sampleDao.create(sampleEntity);
        final SampleEntity selectedEntity = sampleDao.findOne("id");
        Assert.assertNotNull(selectedEntity);

    }

    @Test
    public void testSelect() {
        final SampleEntity sampleEntity = sampleDao.findOne("to-select");
        Assert.assertNotNull(sampleEntity);
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback
    public void testDelete() {
        sampleDao.deleteById("to-delete");
        final SampleEntity sampleEntity = sampleDao.findOne("to-delete");
        Assert.assertNull(sampleEntity);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback
    public void testUpdate() {
        final SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId("to-select");
        sampleEntity.setName("new-name");
        sampleDao.update(sampleEntity);
        final SampleEntity newSample = sampleDao.findOne("to-select");
        Assert.assertEquals("new-name",newSample.getName());
    }

    @Test
    public void testFindAll() {
        final List<SampleEntity> sampleEntityList = sampleDao.findAll();
        Assert.assertEquals(2, sampleEntityList.size());
    }

}
