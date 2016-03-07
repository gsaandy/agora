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

import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * @author gibugeorge on 07/03/16.
 * @version 1.0
 */
public abstract class AbstractDao<T extends Serializable> {

    private EntityManager entityManager;
    private Class<T> clazz;

    protected void setEntity(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public T findOne(final Object entityId) {
        return entityManager.find(clazz, entityId);
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    public void create(T entity) {
        entityManager.persist(entity);
    }

    public T update(final T entity) {
        return entityManager.merge(entity);
    }

    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final Object entityId) {
        final T entity = findOne(entityId);
        delete(entity);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected Class<T> getEntityClass() {
        return clazz;
    }

    protected T getSingleResult(final Query query) {
        final List<T> results = query.getResultList();
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else {
            return results.get(0);
        }
    }

}
