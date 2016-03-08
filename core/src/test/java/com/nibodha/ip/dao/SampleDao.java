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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class SampleDao extends AbstractDao<SampleEntity> {

    public SampleDao() {
        setEntity(SampleEntity.class);
    }

    @Override
    @PersistenceContext(unitName = "test")
    protected void setEntityManager(final EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

}
